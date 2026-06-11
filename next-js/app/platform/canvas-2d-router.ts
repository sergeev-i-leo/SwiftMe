import {Canvas2DDevice} from "./canvas-2d-device";
import {Router} from "../java_franca/graphics/device/router";
import {Canvas2DPainter} from "./canvas-2d-painter";
import {Page} from "../java_franca/graphics/views/page";

export class Canvas2DRouter extends Router {

  private painter: Canvas2DPainter | null = null;
  private canvas: HTMLCanvasElement | null = null;
  private animationFrameId: number | null = null;
  private lastTickTime: number = 0;

  private boundHandleMouseDown: (e: MouseEvent) => void;
  private boundHandleMouseMove: (e: MouseEvent) => void;
  private boundHandleMouseUp: (e: MouseEvent) => void;
  private boundHandleContextMenu: (e: MouseEvent) => void;

  constructor(device: Canvas2DDevice) {
    super();
    this.device = device;
    this.setDevice(device);

    this.boundHandleMouseDown = this.handleMouseDown.bind(this);
    this.boundHandleMouseMove = this.handleMouseMove.bind(this);
    this.boundHandleMouseUp = this.handleMouseUp.bind(this);
    this.boundHandleContextMenu = this.preventContextMenu.bind(this);
  }

  run(canvas: HTMLCanvasElement, rootPage: Page): void {
    this.canvas = canvas;
    this.painter = new Canvas2DPainter(canvas);
    this.pushPage(rootPage);

    canvas.addEventListener('mousedown', this.boundHandleMouseDown);
    canvas.addEventListener('mousemove', this.boundHandleMouseMove);
    canvas.addEventListener('mouseup', this.boundHandleMouseUp);
    canvas.addEventListener('contextmenu', this.boundHandleContextMenu);

    (this.device as Canvas2DDevice).loadResources().then(() => {
      this.startRepainting();
    });
  }

  destroy(): void {
    this.stopRepainting();

    if (this.canvas) {
      this.canvas.removeEventListener('mousedown', this.boundHandleMouseDown);
      this.canvas.removeEventListener('mousemove', this.boundHandleMouseMove);
      this.canvas.removeEventListener('mouseup', this.boundHandleMouseUp);
      this.canvas.removeEventListener('contextmenu', this.boundHandleContextMenu);
      this.canvas = null;
    }

    this.painter = null;
  }

  private preventContextMenu(e: MouseEvent): void {
    e.preventDefault();
  }

  startRepainting(): void {
    if (this.animationFrameId !== null) return;
    this.lastTickTime = performance.now();
    this.animationFrameId = requestAnimationFrame(this.tick.bind(this));
  }

  private tick(now: number): void {
    if (!this.painter || !this.canvas) {
      this.stopRepainting();
      return;
    }

    if (now - this.lastTickTime < 16) {
      this.animationFrameId = requestAnimationFrame(this.tick.bind(this));
      return;
    }
    this.lastTickTime = now;

    const needsRedraw = this.needsRepainting();

    if (needsRedraw) {
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

  private handleMouseDown(e: MouseEvent): void {
    if (!this.canvas) {
      return;
    }

    const rect = this.canvas.getBoundingClientRect();
    const scaleX = this.canvas.width / rect.width;
    const scaleY = this.canvas.height / rect.height;

    const canvasX = (e.clientX - rect.left) * scaleX;
    const canvasY = (e.clientY - rect.top) * scaleY;

    let button = 0;
    if (e.button === 0) button = 1;
    else if (e.button === 2) button = 3;

    this.handlePointerDown(canvasX, canvasY, button);
  }

  private handleMouseMove(e: MouseEvent): void {
  }

  private handleMouseUp(e: MouseEvent): void {
  }
}
