package com.werb.pickphotoview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.werb.pickphotoview.adapter.PickListAdapter;
import com.werb.pickphotoview.model.PickData;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.widget.MyToolbar;

/**
 * Created by wanbo on 2017/1/3.
 */

public class PickListActivity extends AppCompatActivity {

    private PickData pickData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_activity_pick_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PickConfig.INTENT_PICK_DATA);
        initToolbar();
        initRecyclerView();
    }

    private void initToolbar(){
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(pickData.getStatusBarColor());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(pickData.isLightStatusBar()) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        MyToolbar myToolbar = (MyToolbar) findViewById(R.id.toolbar);
        myToolbar.setBackgroundColor(pickData.getToolbarColor());
        myToolbar.setIconColor(pickData.getToolbarIconColor());
        myToolbar.setPhotoDirName(getString(R.string.pick_photos));
        myToolbar.setLeftIcon(R.mipmap.pick_ic_back);
        myToolbar.setLeftLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickListActivity.this.finish();
            }
        });
    }

    private void initRecyclerView(){
        RecyclerView listPhoto = (RecyclerView) findViewById(R.id.photo_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listPhoto.setLayoutManager(layoutManager);
        PickListAdapter listAdapter = new PickListAdapter(PickListActivity.this,listener);
        listPhoto.setAdapter(listAdapter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.pick_finish_slide_out_left);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dirName = (String) v.getTag(R.id.pick_dir_name);
            Intent intent = new Intent();
            intent.setClass(PickListActivity.this, PickPhotoActivity.class);
            intent.putExtra(PickConfig.INTENT_DIR_NAME, dirName);
            PickListActivity.this.setResult(PickConfig.LIST_PHOTO_DATA, intent);
            PickListActivity.this.finish();
        }
    };

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if(intent.getComponent().getClassName().equals(PickPhotoActivity.class.getName())) {
            overridePendingTransition(R.anim.pick_start_slide_in_left, 0);
        }
    }
}
