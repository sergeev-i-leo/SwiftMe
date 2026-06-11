import {Painter} from "../java_franca/graphics/device/painter";
import {SkiaDevice} from "./skia-device";

export class SkiaPainter extends Painter {

  private surface: any = null;
  private canvasKit: any = null;
  private readonly canvasElement: HTMLCanvasElement;
  private skiaDevice: SkiaDevice;

  constructor(canvasElement: HTMLCanvasElement, skiaDevice: SkiaDevice) {
    super();
    this.canvasElement = canvasElement;
    this.skiaDevice = skiaDevice;
    this.canvasKit = skiaDevice.getCanvasKit();
  }

  private ensureSurface(): void {
    if (this.surface) {
      return;
    }

    if (!this.canvasKit) {
      this.canvasKit = this.skiaDevice.getCanvasKit();
    }

    if (!this.canvasKit) {
      console.warn('CanvasKit not ready');
      return;
    }

    this.surface = this.canvasKit.MakeWebGLCanvasSurface(this.canvasElement);
  }

  paintText(text: string, x: number, y: number, deviceFontKey: string, deviceColor: number): void {
    this.ensureSurface();
    if (!this.surface) {
      return;
    }

    const canvas = this.surface.getCanvas();
    const font = this.skiaDevice.getFont('andika_regular');

    const paint = new this.canvasKit.Paint();
    const alpha = deviceColor / 255;
    paint.setColor(this.canvasKit.Color(0, 0, 0, alpha));

    const skFont = new this.canvasKit.Font(font, 16);
    canvas.drawText(text, x, y, paint, skFont);
  }

  drawRect(x: number, y: number, width: number, height: number): void {
    this.ensureSurface();
    if (!this.surface) return;

    const canvas = this.surface.getCanvas();
    const paint = new this.canvasKit.Paint();
    paint.setColor(this.canvasKit.Color(52, 152, 219, 1));
    paint.setStyle(this.canvasKit.PaintStyle.Fill);

    const rect = this.canvasKit.XYWHRect(x, y, width, height);
    canvas.drawRect(rect, paint);
  }

  drawCircle(x: number, y: number, radius: number): void {
    this.ensureSurface();
    if (!this.surface) return;

    const canvas = this.surface.getCanvas();
    const paint = new this.canvasKit.Paint();
    paint.setColor(this.canvasKit.Color(231, 76, 60, 1));
    paint.setStyle(this.canvasKit.PaintStyle.Fill);

    canvas.drawCircle(x, y, radius, paint);
  }

  clear(color?: string): void {
    this.ensureSurface();
    if (!this.surface) return;

    const canvas = this.surface.getCanvas();
    canvas.clear(this.canvasKit.Color(255, 255, 255, 1));
  }

  flush(): void {
    if (this.surface) {
      this.surface.flush();
    }
  }
}
