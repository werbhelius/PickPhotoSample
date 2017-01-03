package com.werb.pickphotoview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;


import com.werb.pickphotoview.adapter.PickGridAdapter;
import com.werb.pickphotoview.adapter.SpaceItemDecoration;
import com.werb.pickphotoview.model.GroupImage;
import com.werb.pickphotoview.model.PickData;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickPhotoHelper;
import com.werb.pickphotoview.util.PickPreferences;
import com.werb.pickphotoview.util.PickUtils;
import com.werb.pickphotoview.util.RxBus;
import com.werb.pickphotoview.util.event.ImageLoadOkEvent;
import com.werb.pickphotoview.widget.MyToolbar;

import java.util.List;

/**
 * Created by wanbo on 2016/12/30.
 */

public class PickPhotoActivity extends AppCompatActivity {

    private PickData pickData;
    private String dirName;
    private RecyclerView photoList;
    private PickGridAdapter pickGridAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PickConfig.INTENT_PICK_DATA);
        dirName = getIntent().getStringExtra(PickConfig.INTENT_DIR_NAME);
        if(pickData != null){
            PickPreferences.getInstance().savePickData(pickData);
        }else {
            pickData = PickPreferences.getInstance().getPickData();
        }
        initToolbar();
        initRecyclerView();
        intiEvent();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.finish_slide_out_bottom);
    }

    private void initToolbar() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        MyToolbar myToolbar = (MyToolbar) findViewById(R.id.toolbar);
        myToolbar.setLeftIcon(R.mipmap.ic_open);
        myToolbar.setRightIcon(R.mipmap.ic_close);
        myToolbar.setPhotoDirName(getString(R.string.all_photo));
        myToolbar.setLeftLayoutOnClickListener(v -> startPhotoListActivity());
        myToolbar.setRightLayoutOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        photoList = (RecyclerView) findViewById(R.id.photo_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, pickData.getSpanCount());
        photoList.setLayoutManager(layoutManager);
        photoList.addItemDecoration(new SpaceItemDecoration(PickUtils.dp2px(PickConfig.ITEM_SPACE),pickData.getSpanCount()));
        if(PickUtils.isEmpty(dirName)) {
            PickPhotoHelper helper = new PickPhotoHelper(PickPhotoActivity.this);
            helper.getImages();
        }else {
            GroupImage listImage = PickPreferences.getInstance().getListImage();
            List<String> photos = listImage.mGroupMap.get(dirName);
            pickGridAdapter = new PickGridAdapter(photos, pickData.isShowCamera(), pickData.getSpanCount());
            photoList.setAdapter(pickGridAdapter);
        }
    }

    private void intiEvent() {
        RxBus.getInstance().toObservable(ImageLoadOkEvent.class).subscribe(imageLoadOkEvent -> {
            GroupImage groupImage = PickPreferences.getInstance().getListImage();
            List<String> photos = groupImage.mGroupMap.get(PickConfig.ALL_PHOTOS);
            Log.d("All photos size:", photos.size() + "");
            pickGridAdapter = new PickGridAdapter(photos, pickData.isShowCamera(), pickData.getSpanCount());
            photoList.setAdapter(pickGridAdapter);
        });
    }

    private void startPhotoListActivity(){
        Intent intent = new Intent();
        intent.setClass(PickPhotoActivity.this,PickListActivity.class);
        startActivity(intent);
        finish();
    }

}
