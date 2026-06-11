import { Device, Painter } from './Device';

export abstract class Page {
  protected router: Router;

  constructor(router: Router) {
    this.router = router;
  }

  abstract paint(device: Device, painter: Painter): void;

  abstract onTouchStart(x: number, y: number): void;
  abstract onTouchMove(x: number, y: number): void;
  abstract onTouchEnd(x: number, y: number): void;

  protected requestRedraw(): void {
    this.router.requestRedraw();
  }
}
