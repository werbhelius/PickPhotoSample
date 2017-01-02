package com.werb.pickphotoview.util;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.werb.pickphotoview.MyApp;

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

    public static int getWidthPixels(){
        DisplayMetrics displayMetrics = MyApp.getApp().getResources().getDisplayMetrics();
        Configuration cf = MyApp.getApp().getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    public static int getHeightPixels(){
        DisplayMetrics displayMetrics = MyApp.getApp().getResources().getDisplayMetrics();
        Configuration cf = MyApp.getApp().getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.widthPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.heightPixels;
        }
        return 0;
    }

    public static int dp2px(float dpValue) {
        final float scale = MyApp.getApp().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dp(float pxValue) {
        final float scale = MyApp.getApp().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }



    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap) {

        int edgeLength = getWidthPixels();

        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg >= 0 && heightOrg >= 0) {
            //从图中截取正中间的正方形部分。
            int xTopLeft = 0;
            int yTopLeft = (getHeightPixels()-getWidthPixels());
            yTopLeft = yTopLeft/2;

            try {
                result = Bitmap.createBitmap(result, xTopLeft, yTopLeft, edgeLength, edgeLength);
                bitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }
        return result;
    }
}
