import {SkiaDevice} from "./skia-device";
import {Page} from "../java_franca/graphics/views/page";
import {Router} from "../java_franca/graphics/device/router";
import {SkiaPainter} from "./skia-painter";

export class SkiaRouter extends Router {

  private painter: SkiaPainter | null = null;
  private canvas: HTMLCanvasElement | null = null;
  private animationFrameId: number | null = null;
  private lastTickTime: number = 0;

  private boundHandleMouseDown: (e: MouseEvent) => void;
  private boundHandleMouseMove: (e: MouseEvent) => void;
  private boundHandleMouseUp: (e: MouseEvent) => void;

  constructor(device: SkiaDevice) {
    super();
    this.device = device;
    this.setDevice(device);

    this.boundHandleMouseDown = this.handleMouseDown.bind(this);
    this.boundHandleMouseMove = this.handleMouseMove.bind(this);
    this.boundHandleMouseUp = this.handleMouseUp.bind(this);
  }

  run(canvas: HTMLCanvasElement, rootPage: Page): void {
    this.canvas = canvas;
    this.pushPage(rootPage);

    (this.device as SkiaDevice).loadResources().then(() => {
      this.painter = new SkiaPainter(canvas, this.device as SkiaDevice);
      this.requestRepainting();
    });

    this.canvas.addEventListener('mousedown', this.boundHandleMouseDown);
    this.canvas.addEventListener('mousemove', this.boundHandleMouseMove);
    this.canvas.addEventListener('mouseup', this.boundHandleMouseUp);

    this.canvas.setAttribute('oncontextmenu', 'return false;');
  }

  startRepainting(): void {
    if (this.animationFrameId !== null) {
      return;
    }
    this.lastTickTime = performance.now();
    this.animationFrameId = requestAnimationFrame(this.tick.bind(this));
  }

  private tick(now: number): void {
    if ((!this.painter) || (!this.canvas)) {
      this.stopRepainting();
      return;
    }

    if (now - this.lastTickTime < 16) {
      this.animationFrameId = requestAnimationFrame(this.tick.bind(this));
      return;
    }
    this.lastTickTime = now;

    const needsRepainting = this.needsRepainting();

    if (needsRepainting) {
      this.painter.clear('#f0f0f0');
      if (this.topPage) {
        this.topPage.paint(this.painter);
      }
      this.painter.flush();
    }

    if (this.needsNextRepainting()) {
      this.animationFrameId = requestAnimationFrame(this.tick.bind(this));
    } else {
      this.stopRepainting();
    }
  }

  private stopRepainting(): void {
    if (this.animationFrameId !== null) {
      cancelAnimationFrame(this.animationFrameId);
      this.animationFrameId = null;
    }
  }

  destroy(): void {
    this.stopRepainting();
    this.canvas?.removeEventListener('mousedown', this.boundHandleMouseDown);
    this.canvas?.removeEventListener('mousemove', this.boundHandleMouseMove);
    this.canvas?.removeEventListener('mouseup', this.boundHandleMouseUp);

    this.painter = null;
    this.canvas = null;
  }

  private handleMouseDown(e: MouseEvent): void {
    if (!this.canvas) return;

    const rect = this.canvas.getBoundingClientRect();
    const scaleX = this.canvas.width / rect.width;
    const scaleY = this.canvas.height / rect.height;

    const canvasX = (e.clientX - rect.left) * scaleX;
    const canvasY = (e.clientY - rect.top) * scaleY;

    let button = 0;
    if (e.button === 0) button = 1;      // левая
    else if (e.button === 2) button = 3; // правая

    this.handlePointerDown(canvasX, canvasY, button);
  }

  private handleMouseMove(e: MouseEvent): void {
    // Будет нужно для drag-and-drop
  }

  private handleMouseUp(e: MouseEvent): void {
    // Будет нужно для drag-and-drop
  }

}
