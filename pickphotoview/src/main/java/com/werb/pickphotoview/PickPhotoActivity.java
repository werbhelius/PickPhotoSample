package com.werb.pickphotoview;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.werb.pickphotoview.adapter.PickAdapter;
import com.werb.pickphotoview.adapter.SpaceItemDecoration;
import com.werb.pickphotoview.util.PickPhotoHelper;
import com.werb.pickphotoview.util.PickUtils;
import com.werb.pickphotoview.util.RxBus;
import com.werb.pickphotoview.util.event.ImageLoadOkEvent;

import java.util.List;

import static com.werb.pickphotoview.R.id.toolbar_cancel;

/**
 * Created by wanbo on 2016/12/30.
 */

public class PickPhotoActivity extends AppCompatActivity {

    private PickData pickData;
    private RecyclerView photoList;
    private PickPhotoHelper helper;
    private PickAdapter pickAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PickConfig.INTENT_PICK_DATA);
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
        FrameLayout back = (FrameLayout) findViewById(R.id.toolbar_back);
        FrameLayout cancel = (FrameLayout) findViewById(toolbar_cancel);
        ImageView iconBack = (ImageView) findViewById(R.id.toolbar_back_icon);
        ImageView iconCancel = (ImageView) findViewById(R.id.toolbarc_cancel_icon);
        iconBack.setColorFilter(getResources().getColor(R.color.white));
        iconCancel.setColorFilter(getResources().getColor(R.color.white));
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        back.setOnClickListener(v -> {

        });
        cancel.setOnClickListener(v -> finish());
    }

    private void initRecyclerView() {
        photoList = (RecyclerView) findViewById(R.id.photo_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, pickData.getSpanCount());
        photoList.setLayoutManager(layoutManager);
        photoList.addItemDecoration(new SpaceItemDecoration(PickUtils.dp2px(PickConfig.ITEM_SPACE),pickData.getSpanCount()));
        helper = new PickPhotoHelper(PickPhotoActivity.this);
        helper.getImages();
    }

    private void intiEvent() {
        RxBus.getInstance().toObservable(ImageLoadOkEvent.class).subscribe(imageLoadOkEvent -> {
            List<String> photos = helper.mGroupMap.get(PickConfig.ALL_PHOTOS);
            Log.d("All photos size:", photos.size() + "");
            pickAdapter = new PickAdapter(photos, pickData.isShowCamera(), pickData.getSpanCount());
            photoList.setAdapter(pickAdapter);
        });
    }

}
