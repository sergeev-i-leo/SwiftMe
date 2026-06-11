import {Canvas2DDevice} from "./canvas-2d-device";
import {Router} from "../java_franca/graphics/device/router";
import {Canvas2DPainter} from "./canvas-2d-painter";
import {Page} from "../java_franca/graphics/views/page";

export class Canvas2DRouter extends Router {

  private painter: Canvas2DPainter | null = null;
  private canvas: HTMLCanvasElement | null = null;
  private animationFrameId: number | null = null;
  private lastTickTime: number = 0;

  constructor(device: Canvas2DDevice) {
    super();
    this.device = device;
    this.setDevice(device);
  }

  // Главный метод: настройка и запуск
  run(canvas: HTMLCanvasElement, rootPage: Page): void {
    this.canvas = canvas;
    this.painter = new Canvas2DPainter(canvas);
    this.pushPage(rootPage);

    // Загружаем ресурсы и запускаем цикл
    (this.device as Canvas2DDevice).loadResources().then(() => {
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

    // FPS лимитер (~60 fps)
    if (now - this.lastTickTime < 16) {
      this.animationFrameId = requestAnimationFrame(this.tick.bind(this));
      return;
    }
    this.lastTickTime = now;

    // Обновляем состояние твинов
    const needsRedraw = this.needsRepainting();

    // Рисуем, если нужно
    if (needsRedraw) {
      this.painter.clear('#f0f0f0');
      if (this.topPage) {
        this.topPage.paint(this.painter);
      }
      this.painter.flush();
    }

    // Продолжаем цикл, если нужны ещё кадры
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

  // Прокси для событий мыши
  handleMouseDown(x: number, y: number, button: number): void {
    this.handlePointerDown(x, y, button);
  }
}
