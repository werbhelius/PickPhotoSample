package com.werb.pickphotoview;

import android.graphics.Color;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by wanbo on 2016/12/30.
 */

public class PickData implements Serializable{

    private int pickPhotoSize;
    private int themeColor;
    private int spanCount;
    private boolean isShowCamera;

    public int getPickPhotoSize() {
        return pickPhotoSize;
    }

    public void setPickPhotoSize(int pickPhotoSize) {
        if(pickPhotoSize > 0 && pickPhotoSize <= PickConfig.DEFAULT_PICK_SIZE){
            this.pickPhotoSize = pickPhotoSize;
        }else {
            Log.e(PickConfig.TAG,"Untrue size : photo size must between 1 and 9");
            this.pickPhotoSize = PickConfig.DEFAULT_PICK_SIZE;
        }
    }

    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        if(spanCount > 0 && spanCount <= PickConfig.DEFAULT_SPAN_COUNT ) {
            this.spanCount = spanCount;
        }else {
            Log.e(PickConfig.TAG,"Untrue count : span count must between 1 and 4");
            this.spanCount = PickConfig.DEFAULT_SPAN_COUNT;
        }
    }

    public boolean isShowCamera() {
        return isShowCamera;
    }

    public void setShowCamera(boolean showCamera) {
        isShowCamera = showCamera;
    }
}
