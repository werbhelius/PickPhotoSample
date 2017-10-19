package com.werb.pickphotoview.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.werb.pickphotoview.model.DirImage;
import com.werb.pickphotoview.model.GroupImage;

/**
 * Created by wanbo on 2017/1/3.
 */

public class PickPreferences {

    private static PickPreferences mInstance = null;
    private final SharedPreferences mSharedPreferences;
    private Context context;

    private static final String IMAGE_LIST = "image_list";
    private static final String DIR_NAMES = "dir_names";
    private static final String PICK_DATA = "pick_data";
    private GroupImage listImage;
    private DirImage dirImage;

    public static PickPreferences getInstance(Context context) {
        if (mInstance == null) {
            synchronized (PickPreferences.class) {
                if (mInstance == null) {
                    mInstance = new PickPreferences(context);
                }
            }
        }
        return mInstance;
    }

    private PickPreferences(Context context) {
        this.context = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean saveImageList(GroupImage images){
        listImage = images;
        Editor editor = mSharedPreferences.edit();
        editor.putString(IMAGE_LIST, PickGson.INSTANCE.toJson(images));
        boolean result = editor.commit();
        return result;
    }

    public GroupImage getListImage(){
        if(listImage == null) {
            String ss = mSharedPreferences.getString(IMAGE_LIST, "");
            if(TextUtils.isEmpty(ss)) {
                return null;
            } else {
                listImage = PickGson.INSTANCE.fromJson(GroupImage.class, ss);
            }
        }
        return listImage;
    }

    public boolean saveDirNames(DirImage images){
        dirImage = images;
        Editor editor = mSharedPreferences.edit();
        editor.putString(DIR_NAMES, PickGson.INSTANCE.toJson(images));
        return editor.commit();
    }

    public DirImage getDirImage(){
        if(dirImage == null) {
            String ss = mSharedPreferences.getString(DIR_NAMES, "");
            if(TextUtils.isEmpty(ss)) {
                return null;
            } else {
                dirImage = PickGson.INSTANCE.fromJson(DirImage.class, ss);
            }
        }
        return dirImage;
    }

}
