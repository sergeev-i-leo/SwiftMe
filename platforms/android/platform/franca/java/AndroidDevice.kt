package franca.java.android_test_application

import franca.java.graphics.device.Device

class AndroidDevice(
) : Device() {

  override fun getTime(): Long = System.currentTimeMillis()
}