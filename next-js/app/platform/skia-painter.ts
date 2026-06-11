import {Painter} from "../java_franca/graphics/device/painter";

export class SkiaPainter extends Painter {

  private canvas: HTMLCanvasElement;
  private ctx: CanvasRenderingContext2D | null = null;

  constructor(canvas: HTMLCanvasElement) {
    super();
    this.canvas = canvas;
    // Пока используем Canvas 2D как заглушку
    this.ctx = canvas.getContext('2d');
  }

  paintText(text: string, x: number, y: number, deviceFontKey: string, deviceColor: number): void {
    if (!this.ctx) return;

    this.ctx.fillStyle = `rgba(0, 0, 0, ${deviceColor / 255})`;
    this.ctx.font = '16px sans-serif';
    this.ctx.fillText(text, x, y);
  }

  drawRect(x: number, y: number, width: number, height: number): void {
    if (!this.ctx) return;
    this.ctx.fillStyle = '#3498db';
    this.ctx.fillRect(x, y, width, height);
  }

  drawCircle(x: number, y: number, radius: number): void {
    if (!this.ctx) return;
    this.ctx.beginPath();
    this.ctx.arc(x, y, radius, 0, Math.PI * 2);
    this.ctx.fillStyle = '#e74c3c';
    this.ctx.fill();
  }

  clear(color?: string): void {
    if (!this.ctx) return;
    this.ctx.fillStyle = color || '#ffffff';
    this.ctx.fillRect(0, 0, this.canvas.width, this.canvas.height);
  }

  flush(): void {
    // для Canvas 2D не требуется
  }
}
