package franca.java.skia_applications

import franca.java.contracted.JavaDevice

class SkiaDevice(private val skiaTestApplication: SkiaTestApplication) : JavaDevice() {

  override fun getTime(): Long = System.currentTimeMillis()

  override fun startRepainting() {
    skiaTestApplication.startRepainting();
  }
}
