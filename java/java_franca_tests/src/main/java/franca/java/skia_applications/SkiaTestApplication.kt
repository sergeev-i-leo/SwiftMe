package franca.java.skia_applications

import org.jetbrains.skiko.*
import franca.java.graphics.views.Page
import franca.java.graphics.test_components.TestView0
import java.util.concurrent.ScheduledExecutorService

class SkiaTestApplication {

  private val skiaLayer = SkiaLayer()
  private val skiaDevice = JavaDevice()

  private var scheduler: ScheduledExecutorService? = null
  private var lastTickTime = 0L

  companion object {
    @JvmStatic
    fun main(args: Array<String>) {
      val skiaRouter = SkiaRouter(skiaDevice, this)
      val page = Page(skiaRouter)
      page.views.add(TestView0())

      SkiaTestApplication()
    }
  }
}

