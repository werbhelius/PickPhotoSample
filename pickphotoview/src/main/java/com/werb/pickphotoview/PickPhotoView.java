package com.werb.pickphotoview;

import android.app.Activity;
import android.content.Intent;

import com.werb.pickphotoview.model.PickData;
import com.werb.pickphotoview.util.PickConfig;

/**
 * Created by wanbo on 2016/12/30.
 */

public class PickPhotoView {

    private PickData pickData;
    private Activity activity;

    PickPhotoView(Builder builder){
        pickData = builder.pickData;
        activity = builder.activity;
    }

    private void start(){
        Intent intent = new Intent();
        intent.setClass(activity,PickPhotoActivity.class);
        intent.putExtra(PickConfig.INTENT_PICK_DATA,pickData);
        activity.startActivityForResult(intent,PickConfig.PICK_PHOTO_DATA);
    }

    public static class Builder{

        private PickData pickData;
        private Activity activity;

        public Builder(Activity a) {
            pickData = new PickData();
            activity = a;
        }

        public Builder setPickPhotoSize(int photoSize) {
            pickData.setPickPhotoSize(photoSize);
            return this;
        }

        private Builder setThemeColor(int themeColor) {
            pickData.setThemeColor(themeColor);
            return this;
        }

        public Builder setSpanCount(int spanCount) {
            pickData.setSpanCount(spanCount);
            return this;
        }

        public Builder setShowCamera(boolean showCamera) {
            pickData.setShowCamera(showCamera);
            return this;
        }

        private PickPhotoView create(){
            return new PickPhotoView(this);
        }

        public void start(){
            create().start();
        }

    }
}
