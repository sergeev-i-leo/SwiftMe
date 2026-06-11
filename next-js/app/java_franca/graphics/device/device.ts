
export class Device {
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
}
