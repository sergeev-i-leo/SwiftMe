package me.swift.android_test_application

import android.app.Activity
import android.os.Bundle
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : Activity() {

  private lateinit var androidNavigator: AndroidNavigator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val container = FrameLayout(this).apply {
      layoutParams = FrameLayout.LayoutParams(
        FrameLayout.LayoutParams.MATCH_PARENT,
        FrameLayout.LayoutParams.MATCH_PARENT
      )
    }

    val bottomNav = BottomNavigationView(this).apply {
      inflateMenu(R.menu.bottom_nav_menu)
    }

    val root = FrameLayout(this).apply {
      addView(container)
      addView(bottomNav)
    }

    setContentView(root)

    androidNavigator = AndroidNavigator(this, container, bottomNav)
  }
}