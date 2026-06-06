package franca.typescript.swing_applications;

import franca.typescript.step_gs.renderer.Page;
import franca.typescript.contract.JavaDevice;

public class SwingDevice extends JavaDevice {

  SwingApplication swingApplication;

  public SwingDevice(SwingApplication swingApplication) {
    super();

    this.swingApplication = swingApplication;
  }

  @Override
  public long getTime() {
    return System.nanoTime() / 1_000_000;
  }

  @Override
  public void startRepainting(Page page) {
    swingApplication.startRepainting();
  }
}
