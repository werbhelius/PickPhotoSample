package com.werb.pickphotoview.util;

import android.text.TextUtils;

/**
 * Created by wanbo on 2016/12/30.
 */

public class PickUtils {

    public static boolean isEmpty(String src) {
        if (TextUtils.isEmpty(src)) {
            return true;
        } else {
            return false;
        }
    }
}
