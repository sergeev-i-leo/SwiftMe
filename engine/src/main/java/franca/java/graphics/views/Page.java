package franca.java.graphics.views;

import franca.java.core.contracted.TranspilableClass;
import franca.java.core.contracted.ContractedArray;
import franca.java.graphics.device.Device;
import franca.java.graphics.painter.Painter;

/**
 * Page manages all animations.
 * Ownership rule: whoever creates the animation (via registerAnimation) does NOT own it — Page owns it.
 * Page calls destroy() when removing animation from the chain.
 * External code must NEVER call destroy() on animations directly.
 */

public class Page extends TranspilableClass {

  Device device = null;

  public ContractedArray<View> views = new ContractedArray<View>();

  public Page() {
  }

  @Override
  public void destroy() {
    delete(views);
    views = null;
    super.destroy();
  }

  public void setDevice(Device device) {
    this.device = device;
  }

  public void paint(Painter painter) {
    if (device == null) {
      return;
    }

    for (int i = 0; i < views.size(); i++) {
      views.get(i).paint(device, painter, this);
    }
  }

  public void handlePointerDown(float pointedX, float pointedY, int buttonNumber) {
    if (device == null) {
      return;
    }

    for (int i = 0; i < views.size(); i++) {
      views.get(i).handlePointerDown(device, this, 0f, 0f, pointedX, pointedY, buttonNumber);
    }
  }
}
