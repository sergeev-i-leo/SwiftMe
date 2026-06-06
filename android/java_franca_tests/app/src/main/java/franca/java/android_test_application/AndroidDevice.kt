package franca.java.android_test_application

import franca.java.graphics.device.Device
import franca.java.graphics.renderer.Page
import me.swift.android_test_application.AndroidDeviceView
import me.swift.android_test_application.AndroidNavigator


class AndroidDevice(
  private val androidNavigator: AndroidNavigator
) : Device() {

  override fun getTime(): Long = System.currentTimeMillis()

  override fun startRepainting(page: Page) {
    val currentView = androidNavigator.currentView

    if ((currentView is AndroidDeviceView) && (currentView.getPage() === page)) {
      currentView.startRepainting()
    }
  }
}