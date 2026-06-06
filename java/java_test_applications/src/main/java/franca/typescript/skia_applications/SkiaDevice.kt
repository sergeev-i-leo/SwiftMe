package franca.typescript.skia_applications

import franca.typescript.contract.JavaDevice
import franca.typescript.step_gs.renderer.Page

class SkiaDevice(private val skiaTestApplication: SkiaTestApplication) : JavaDevice() {

  override fun getTime(): Long = System.currentTimeMillis()

  override fun startRepainting(page: Page) {
    skiaTestApplication.startRepainting();
  }
}
