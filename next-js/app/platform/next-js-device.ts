export class Device {
  private static instance: Device;
  private router: Router | null = null;
  private skia: any = null;
  private fonts: Map<string, any> = new Map();

  private constructor() {}

  static getInstance(): Device {
    if (!Device.instance) {
      Device.instance = new Device();
    }
    return Device.instance;
  }

  async loadResources(): Promise<void> {
    // Загрузка Skia и шрифтов
  }

  setRouter(router: Router): void {
    this.router = router;
  }

  getRouter(): Router | null {
    return this.router;
  }

  createPainter(canvas: HTMLCanvasElement): Painter {
    // Создаёт платформозависимый Painter (WebGL/Canvas2D)
    return new WebPainter(canvas, this.skia);
  }

  getFont(path: string): any {
    return this.fonts.get(path);
  }
}
