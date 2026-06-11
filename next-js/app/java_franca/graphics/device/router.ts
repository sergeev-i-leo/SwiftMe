import { Device, Painter } from './Device';
import { Page } from './Page';

export class Router {
  private pages: Page[] = [];
  private activePages: Page[] = [];
  private device: Device;
  private painter: Painter | null = null;
  private animationFrameId: number | null = null;
  private needRedraw = false;

  constructor(device: Device) {
    this.device = device;
    device.setRouter(this);
  }

  addPage(page: Page): void {
    this.pages.push(page);
  }

  activatePage(page: Page): void {
    if (!this.activePages.includes(page)) {
      this.activePages.push(page);
      this.requestRedraw();
    }
  }

  deactivatePage(page: Page): void {
    const index = this.activePages.indexOf(page);
    if (index !== -1) {
      this.activePages.splice(index, 1);
      this.requestRedraw();
    }
  }

  requestRedraw(): void {
    this.needRedraw = true;
    this.startRenderLoop();
  }

  private startRenderLoop(): void {
    if (this.animationFrameId !== null) return;

    const loop = () => {
      if (this.needRedraw && this.painter) {
        this.needRedraw = false;
        this.paint();
      }
      this.animationFrameId = requestAnimationFrame(loop);
    };
    this.animationFrameId = requestAnimationFrame(loop);
  }

  private paint(): void {
    if (!this.painter) return;

    for (const page of this.activePages) {
      page.paint(this.device, this.painter);
    }
    this.painter.flush();
  }

  setPainter(painter: Painter): void {
    this.painter = painter;
    this.requestRedraw();
  }

  stopRenderLoop(): void {
    if (this.animationFrameId !== null) {
      cancelAnimationFrame(this.animationFrameId);
      this.animationFrameId = null;
    }
  }
}
