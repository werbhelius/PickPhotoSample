package com.werb.pickphotosample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.werb.pickphotoview.PickPhotoView;
import com.werb.pickphotoview.adapter.SpaceItemDecoration;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickUtils;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SampleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.click).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPickPhoto();
            }
        });

        RecyclerView photoList = (RecyclerView) findViewById(R.id.photo_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        photoList.setLayoutManager(layoutManager);
        photoList.addItemDecoration(new SpaceItemDecoration(PickUtils.dp2px(PickConfig.ITEM_SPACE), 4));
        adapter = new SampleAdapter(this,null);
        photoList.setAdapter(adapter);
    }

    private void startPickPhoto(){
        new PickPhotoView.Builder(MainActivity.this)
                .setPickPhotoSize(9)
                .setShowCamera(false)
                .setSpanCount(4)
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 0){
            return;
        }
        if(data == null){
            return;
        }
        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            List<String> selectPaths = (List<String>) data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
            adapter.updateData(selectPaths);
        }
    }
}
