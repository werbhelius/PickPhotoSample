package com.werb.pickphotoview.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by wanbo on 2016/12/30.
 */

public class PickUtils {

    private static PickUtils mInstance = null;
    private Context context;

    public static PickUtils getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PickPreferences.class) {
                if (mInstance == null) {
                    mInstance = new PickUtils(context);
                }
            }
        }
        return mInstance;
    }

    private PickUtils(Context context) {
        this.context = context;
    }


    public boolean isEmpty(String src) {
        if (TextUtils.isEmpty(src)) {
            return true;
        } else {
            return false;
        }
    }

    public int getWidthPixels() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.widthPixels;
        }
        return 0;
    }

    public int getHeightPixels() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        Configuration cf = context.getResources().getConfiguration();
        int ori = cf.orientation;
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {// 横屏
            return displayMetrics.widthPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            return displayMetrics.heightPixels;
        }
        return 0;
    }

    public int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private int computeScale(BitmapFactory.Options options, int dstw, int dsth) {
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

    public Bitmap loadBitmap(String path, int maxWidth, int maxHeight) {
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

    private Bitmap matrixBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        float scaleX = getWidthPixels() / (float) bitmap.getWidth();
        float scaleY = getHeightPixels() / (float) bitmap.getHeight();
        float originalScale = Math.min(scaleX, scaleY);
        matrix.setScale(originalScale, originalScale);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public Uri getUri(File file) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                String authority = context.getApplicationInfo().packageName + ".provider";
                Log.d(PickConfig.TAG, "authority:" + authority);
                return FileProvider.getUriForFile(context.getApplicationContext(), authority, file);
            } else {
                return Uri.fromFile(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
