import {Device} from "../java_franca/graphics/device/device";

export class SkiaDevice extends Device {
  private static instance: SkiaDevice;
  private _isReady: boolean = false;

  private constructor() {
    super();
  }

  static getInstance(): SkiaDevice {
    if (!SkiaDevice.instance) {
      SkiaDevice.instance = new SkiaDevice();
    }
    return SkiaDevice.instance;
  }

  async loadResources(): Promise<void> {
    if (this._isReady) return;
    // TODO: загружать CanvasKit и шрифты
    await new Promise(resolve => setTimeout(resolve, 100));
    this._isReady = true;
  }

  isReady(): boolean {
    return this._isReady;
  }

  getTime(): number {
    return performance.now();
  }
}
