package com.werb.pickphotoview.util;

import android.text.TextUtils;

/**
 * Created by wanbo on 2017/3/26.
 */

public class SoleUtils {

    public static boolean isEmpty(String src) {
        if (TextUtils.isEmpty(src)) {
            return true;
        } else {
            return false;
        }
    }
}
