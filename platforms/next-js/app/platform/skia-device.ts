import {Device} from "../java_franca/graphics/device/device";

declare global {
  interface Window {
    CanvasKitInit?: (config: { locateFile: (file: string) => string }) => Promise<any>;
  }
}

export class SkiaDevice extends Device {

  private static instance: SkiaDevice;
  private _isReady: boolean = false;
  private CanvasKit: any = null;
  private fonts: Map<string, any> = new Map();

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
    if (this._isReady) {
      return;
    }

    try {
      await this.loadCanvasKitScript();

      await this.waitForCanvasKitInit();

      this.CanvasKit = await (window as any).CanvasKitInit({
        locateFile: (file: string) => `/canvaskit/${file}`,
      });

      const fontNames = ['andika_regular', 'oswald_bold'];
      for (const fontName of fontNames) {
        try {
          const response = await fetch(`/fonts/${fontName}.ttf`);
          const arrayBuffer = await response.arrayBuffer();
          const typeface = this.CanvasKit.Typeface.MakeFreeTypeFaceFromData(arrayBuffer);
          this.fonts.set(fontName, typeface);
        } catch (e) {
          console.warn(`Font ${fontName} not loaded`, e);
        }
      }

      this._isReady = true;
    } catch (e) {
      console.error('Failed to load CanvasKit', e);
    }
  }

  private loadCanvasKitScript(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (window.CanvasKitInit) {
        resolve();
        return;
      }

      const script = document.createElement('script');
      script.src = '/canvaskit/canvaskit.js';
      script.onload = () => resolve();
      script.onerror = () => reject(new Error('Failed to load CanvasKit script'));
      document.head.appendChild(script);
    });
  }

  private waitForCanvasKitInit(): Promise<void> {
    return new Promise((resolve) => {
      const check = setInterval(() => {
        if (window.CanvasKitInit) {
          clearInterval(check);
          resolve();
        }
      }, 50);
    });
  }

  isReady(): boolean {
    return this._isReady;
  }

  getTime(): number {
    return performance.now();
  }

  getCanvasKit(): any {
    return this.CanvasKit;
  }

  getFont(name: string): any {
    return this.fonts.get(name) || null;
  }
}
