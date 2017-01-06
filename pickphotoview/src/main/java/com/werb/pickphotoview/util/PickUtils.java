package com.werb.pickphotoview.util;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

    public static int getWidthPixels() {
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

    public static int getHeightPixels() {
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

    private static int computeScale(BitmapFactory.Options options, int dstw, int dsth) {
        int inSampleSize = 1;
        if (dstw == 0 || dsth == 0) {
            return inSampleSize;
        }
        int width = options.outWidth;
        int height = options.outHeight;

        if (width > dstw || height > dsth) {
            int heightRatio = Math.round((float) height / (float) dsth);
            int widthRatio = Math.round((float) width / (float) dstw);
            inSampleSize = Math.min(heightRatio, widthRatio);
        }
        return inSampleSize;
    }

    public static Bitmap loadBitmap(String path, int maxWidth, int maxHeight) {
        if (!TextUtils.isEmpty(path)) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                options.inSampleSize = computeScale(options, maxWidth, maxHeight);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                return matrixBitmap(bitmap);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return null;
    }

    private static Bitmap matrixBitmap(Bitmap bitmap){
        Matrix matrix = new Matrix();
        float scaleX = PickUtils.getWidthPixels() / (float) bitmap.getWidth();
        float scaleY = PickUtils.getHeightPixels() / (float) bitmap.getHeight();
        float originalScale = Math.min(scaleX, scaleY);
        matrix.setScale(originalScale, originalScale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

//    private String cacheExists(String url) {
//        try {
//            File fileDir = new File(mCacheRootPath);
//            if(!fileDir.exists()) {
//                fileDir.mkdirs();
//            }
//
//            File file = new File(mCacheRootPath,new StringBuffer().append(MD5EncryptorUtils.md5Encryption(url)).toString());
//            if(file.exists()) {
//                return file.getAbsolutePath();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";
//    }
//
//    public String getCacheNoExistsPath(String url) {
//        File fileDir = new File(mCacheRootPath);
//        if(!fileDir.exists()) {
//            fileDir.mkdirs();
//        }
//
//
//        return new StringBuffer().append(mCacheRootPath)
//                .append(MD5EncryptorUtils.md5Encryption(url)).toString();
//    }

}
