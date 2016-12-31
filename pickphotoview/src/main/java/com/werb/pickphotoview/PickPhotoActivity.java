package com.werb.pickphotoview;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.werb.pickphotoview.util.PickPhotoHelper;
import com.werb.pickphotoview.util.RxBus;
import com.werb.pickphotoview.util.event.ImageLoadOkEvent;

import java.util.List;

import rx.functions.Action1;

import static com.werb.pickphotoview.R.id.toolbar_cancel;

/**
 * Created by wanbo on 2016/12/30.
 */

public class PickPhotoActivity extends AppCompatActivity {

    private PickData pickData;
    private RecyclerView photoList;
    private PickPhotoHelper helper;

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

    private void initToolbar(){
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView(){
        photoList = (RecyclerView) findViewById(R.id.photo_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this,pickData.getSpanCount());
        photoList.setLayoutManager(layoutManager);
        helper = new PickPhotoHelper(PickPhotoActivity.this);
        helper.getImages();
    }

    private void intiEvent(){
        RxBus.getInstance().toObservable(ImageLoadOkEvent.class).subscribe(new Action1<ImageLoadOkEvent>() {
            @Override
            public void call(ImageLoadOkEvent imageLoadOkEvent) {
                List<String> photos = helper.mGroupMap.get(PickConfig.ALL_PHOTOS);
                System.out.println(photos.size() + "");
            }
        });
    }

}
