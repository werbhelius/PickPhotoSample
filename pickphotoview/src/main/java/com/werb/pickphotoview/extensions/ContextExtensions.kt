package com.werb.pickphotoview.extensions

import android.content.Context
import android.graphics.drawable.Drawable

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/15. */

fun Context.drawable(resId: Int) : Drawable = resources.getDrawable(resId)

fun Context.color(resId: Int) : Int = resources.getColor(resId)

fun Context.string(resId: Int) : String = getString(resId)