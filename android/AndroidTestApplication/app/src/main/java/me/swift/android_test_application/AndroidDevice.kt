package me.swift.android_test_application

import android.os.Handler
import android.os.Looper
import android.view.View
import me.swift.engine.Device
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class AndroidDevice(
  private val activity: MainActivity,
  private val view: View
) : Device() {

  private var scheduledExecutorService: ScheduledExecutorService? = null
  private var lastTickTime = 0L
  private val mainHandler = Handler(Looper.getMainLooper())

  @Volatile
  private var isPainting = false

  override fun getTime(): Long {
    return System.currentTimeMillis()
  }

  override fun startRepainting() {
    if (scheduledExecutorService != null) {
      return
    }

    scheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
    lastTickTime = getTime()
    scheduledExecutorService?.scheduleAtFixedRate(
      { tick() },
      0,
      2,
      TimeUnit.MILLISECONDS
    )
  }

  fun onPaintStart() {
    isPainting = true
  }

  fun onPaintEnd() {
    isPainting = false
  }

  private fun tick() {
    val tickTime = time
    if (tickTime - lastTickTime < 16) {
      return
    }

    lastTickTime = tickTime

    if ((!isPainting) && (activity.page.needsRepainting())) {
      mainHandler.post { view.invalidate() }
    }

    if (!activity.page.needsNextRepainting()) {
      scheduledExecutorService?.shutdown()
      scheduledExecutorService = null
    }
  }
}