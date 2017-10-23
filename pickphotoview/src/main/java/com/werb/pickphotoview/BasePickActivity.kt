package com.werb.pickphotoview

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/23. */

open class BasePickActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theme()
    }

    private fun theme() {
        GlobalData.model?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val decorView = window.decorView
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                decorView.systemUiVisibility = option
                window.statusBarColor = Color.TRANSPARENT
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (it.lightStatusBar) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                }
            }
        }
    }

}