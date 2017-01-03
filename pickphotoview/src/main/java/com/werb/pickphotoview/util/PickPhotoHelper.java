package com.werb.pickphotoview.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import com.werb.pickphotoview.model.DirImage;
import com.werb.pickphotoview.model.GroupImage;
import com.werb.pickphotoview.util.event.ImageLoadOkEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wanbo on 2016/12/31.
 */

public class PickPhotoHelper {

    private Activity activity;
    public HashMap<String, List<String>> mGroupMap = new LinkedHashMap<>();

    public PickPhotoHelper(Activity activity) {
        this.activity = activity;
    }

    public void getImages() {
        new Thread(() -> {
            Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = activity.getContentResolver();

            //jpeg & png
            Cursor mCursor = mContentResolver.query(mImageUri, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or "
                            + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

            if(mCursor == null){
                return;
            }
            List<String> dirNames = new ArrayList<>();
            while (mCursor.moveToNext()) {
                // get image path
                String path = mCursor.getString(mCursor
                        .getColumnIndex(MediaStore.Images.Media.DATA));

                // get image parent name
                String parentName = new File(path).getParentFile().getName();
                Log.d(PickConfig.TAG, parentName + ":" + path);
                // save all Photo
                if (!mGroupMap.containsKey(PickConfig.ALL_PHOTOS)) {
                    dirNames.add(PickConfig.ALL_PHOTOS);
                    List<String> chileList = new ArrayList<>();
                    chileList.add(path);
                    mGroupMap.put(PickConfig.ALL_PHOTOS, chileList);
                } else {
                    mGroupMap.get(PickConfig.ALL_PHOTOS).add(path);
                }
                // save by parent name
                if (!mGroupMap.containsKey(parentName)) {
                    dirNames.add(parentName);
                    List<String> chileList = new ArrayList<>();
                    chileList.add(path);
                    mGroupMap.put(parentName, chileList);
                } else {
                    mGroupMap.get(parentName).add(path);
                }
            }
            mCursor.close();
            GroupImage groupImage = new GroupImage();
            groupImage.mGroupMap = mGroupMap;
            DirImage dirImage = new DirImage();
            dirImage.dirName = dirNames;
            PickPreferences.getInstance().saveImageList(groupImage);
            PickPreferences.getInstance().saveDirNames(dirImage);
            r.post(() -> RxBus.getInstance().send(new ImageLoadOkEvent()));
        }).start();

    }

    static android.os.Handler r = new android.os.Handler(Looper.getMainLooper());
}
