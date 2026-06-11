import {SkiaDevice} from "./skia-device";
import {Page} from "../java_franca/graphics/views/page";
import {Router} from "../java_franca/graphics/device/router";
import {SkiaPainter} from "./skia-painter";

export class SkiaRouter extends Router {

  private painter: SkiaPainter | null = null;
  private canvas: HTMLCanvasElement | null = null;
  private animationFrameId: number | null = null;
  private lastTickTime: number = 0;

  constructor(device: SkiaDevice) {
    super();
    this.device = device;
    this.setDevice(device);
  }

  run(canvas: HTMLCanvasElement, rootPage: Page): void {
    this.canvas = canvas;
    this.painter = new SkiaPainter(canvas);
    this.pushPage(rootPage);

    (this.device as SkiaDevice).loadResources().then(() => {
      this.requestRepainting();
      this.startRepainting();
    });
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

  destroy(): void {
    this.stopRepainting();
    this.painter = null;
    this.canvas = null;
  }
}
