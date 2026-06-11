import {Device} from "../java_franca/graphics/device/device";

export class Canvas2DDevice extends Device {

  private static instance: Canvas2DDevice;
  private _isReady: boolean = false;
  private fonts: Map<string, FontFace> = new Map();

  private constructor() {
    super();
  }

  static getInstance(): Canvas2DDevice {
    if (!Canvas2DDevice.instance) {
      Canvas2DDevice.instance = new Canvas2DDevice();
    }
    return Canvas2DDevice.instance;
  }

  async loadResources(): Promise<void> {
    if (this._isReady) {
      return;
    }

    // Загружаем шрифты (пример)
    /*const fontNames = ['Roboto', 'Arial'];
    for (const fontName of fontNames) {
      const font = new FontFace(fontName, `url(/fonts/${fontName}.ttf)`);
      await font.load();
      document.fonts.add(font);
      this.fonts.set(fontName, font);
    }*/

    this._isReady = true;
  }

  isReady(): boolean {
    return this._isReady;
  }

  getTime(): number {
    return performance.now();
  }
}
