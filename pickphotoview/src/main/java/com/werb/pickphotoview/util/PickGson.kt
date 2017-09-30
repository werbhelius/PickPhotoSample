package com.werb.pickphotoview.util

import android.text.TextUtils

import com.google.gson.Gson

/**
 * Created by wanbo on 2017/1/3.
 */

object PickGson {

    private val gson = Gson()

    @Synchronized
    fun <T> fromJson(cls: Class<T>, srcStr: String): T? {
        val result: T?
        if (TextUtils.isEmpty(srcStr)) {
            return null
        }
        result = try {
            gson.fromJson(srcStr, cls)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        return result
    }

    @Synchronized
    fun toJson(any: Any): String {
        return gson.toJson(any)
    }
}
