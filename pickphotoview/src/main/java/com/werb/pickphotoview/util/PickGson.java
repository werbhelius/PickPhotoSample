package com.werb.pickphotoview.util;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * Created by wanbo on 2017/1/3.
 */

public class PickGson {

    private static Gson gson = new Gson();

    public synchronized static <T> T fromJson(Class<T> cls, String srcStr) {
        T result;
        if (TextUtils.isEmpty(srcStr)) {
            return null;
        }
        try {
            result = gson.fromJson(srcStr, cls);
        } catch (Exception e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    public synchronized static String toJson(Object object) {
        return gson.toJson(object);
    }
}
