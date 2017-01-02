package com.werb.pickphotoview;

import android.app.Application;
import android.content.Context;

/**
 * Created by wanbo on 2017/1/2.
 */

public class MyApp extends Application{

    private static volatile MyApp myContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        myContext = this;
    }

    public static MyApp getApp() {
        return myContext;
    }
}
