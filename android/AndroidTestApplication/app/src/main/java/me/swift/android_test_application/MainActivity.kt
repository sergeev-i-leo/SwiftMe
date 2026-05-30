package me.swift.android_test_application

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.swift.engine.Page
import me.swift.engine.test_components.TestView0

class MainActivity : AppCompatActivity() {

  lateinit var device: AndroidDevice
  lateinit var page: Page
  private lateinit var customView: CustomView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    supportActionBar?.hide()
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    customView = CustomView(this)
    setContentView(customView)

    device = AndroidDevice(this, customView)
    page = Page(device)

    page.views.add(TestView0())

    customView.setPage(page)
  }

  inner class CustomView(context: Context) : View(context) {

    private var page: Page? = null
    private val painter = AndroidPainter()

    private var activePointerId = -1
    private var lastX = 0f
    private var lastY = 0f

    init {
      isFocusable = true
      isFocusableInTouchMode = true
    }

    fun setPage(page: Page) {
      this.page = page
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
      val action = event.actionMasked
      val pointerIndex = event.actionIndex

      when (action) {
        MotionEvent.ACTION_DOWN -> {
          // Нажал
          activePointerId = event.getPointerId(pointerIndex)
          val x = event.getX(pointerIndex)
          val y = event.getY(pointerIndex)
          lastX = x
          lastY = y

          page?.handlePointerDown(x, y, 1)
          return true
        }

        MotionEvent.ACTION_MOVE -> {
          // Поехал
          val pointerIndex = event.findPointerIndex(activePointerId)
          if (pointerIndex < 0) return true

          val x = event.getX(pointerIndex)
          val y = event.getY(pointerIndex)
          val dx = x - lastX
          val dy = y - lastY

          if (dx != 0f || dy != 0f) {
            //page?.handlePointerMove(x, y, dx, dy, 0)
            lastX = x
            lastY = y
          }
          return true
        }

        MotionEvent.ACTION_UP -> {
          val pointerIndex = event.findPointerIndex(activePointerId)
          if (pointerIndex >= 0) {
            val x = event.getX(pointerIndex)
            val y = event.getY(pointerIndex)
            //page?.handlePointerUp(x, y, 0)
          }
          activePointerId = -1
          return true
        }

        MotionEvent.ACTION_CANCEL -> {
          //page?.handlePointerCancel(0)
          activePointerId = -1
          return true
        }
      }

      return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {

      var time = device.time

      super.onDraw(canvas)

      device.onPaintStart()

      try {
        painter.setCanvas(canvas)
        page?.paint(painter)
      } finally {
        device.onPaintEnd()
      }

      time = device.time - time
      if (time > 12) {
        Log.w("CustomView", "Slow frame: ${time}ms (threshold: 12ms)")
      }
    }
  }
}