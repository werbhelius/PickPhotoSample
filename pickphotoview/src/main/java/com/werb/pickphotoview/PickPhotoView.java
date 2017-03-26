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

    private PickPhotoView(Builder builder){
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

        public Builder setSpanCount(int spanCount) {
            pickData.setSpanCount(spanCount);
            return this;
        }

        public Builder setShowCamera(boolean showCamera) {
            pickData.setShowCamera(showCamera);
            return this;
        }

        public Builder setToolbarColor(String toolbarColor){
            pickData.setToolbarColor(toolbarColor);
            return this;
        }

        public Builder setStatusBarColor(String statusBarColor){
            pickData.setStatusBarColor(statusBarColor);
            return this;
        }

        public Builder setToolbarIconColor(String toolbarIconColor){
            pickData.setToolbarIconColor(toolbarIconColor);
            return this;
        }

        public Builder setLightStatusBar(boolean lightStatusBar){
            pickData.setLightStatusBar(lightStatusBar);
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
