package com.werb.pickphotoview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;
import com.werb.pickphotoview.model.PickData;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.widget.MyToolbar;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by wanbo on 2017/1/4.
 */

public class PickPhotoPreviewActivity extends AppCompatActivity {

    private List<String> allImagePath;
    private List<String> selectImagePath;
    private String path;
    private ViewPager viewPager;
    private List<LargeImageView> imageViews;
    private MyToolbar myToolbar;
    private boolean mIsHidden,misSelect;
    private PickData pickData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_activty_preview_photo);
        pickData = (PickData) getIntent().getSerializableExtra(PickConfig.INTENT_PICK_DATA);
        path = getIntent().getStringExtra(PickConfig.INTENT_IMG_PATH);
        allImagePath = (List<String>) getIntent().getSerializableExtra(PickConfig.INTENT_IMG_LIST);
        selectImagePath = (List<String>) getIntent().getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT);
        imageViews = new ArrayList<>();
        if(selectImagePath == null){
            selectImagePath = new ArrayList<>();
        }
        for (int i = 0; i < 4; i++) {
            LargeImageView imageView = new LargeImageView(this);
            imageView.setEnabled(true);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideOrShowToolbar();
                }
            });
            imageViews.add(imageView);
        }
        initView();
        Log.d("image size", allImagePath.size() + "");
    }

    private void initView() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(pickData.getStatusBarColor());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(pickData.isLightStatusBar()) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        myToolbar = (MyToolbar) findViewById(R.id.toolbar);
        myToolbar.setBackgroundColor(pickData.getToolbarColor());
        myToolbar.setIconColor(pickData.getToolbarIconColor());
        myToolbar.setLeftIcon(R.mipmap.pick_ic_back);
        myToolbar.setLeftLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishForResult();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.image_vp);
        int indexOf = allImagePath.indexOf(path);
        judgeSelect(allImagePath.get(indexOf));
        viewPager.setAdapter(new listPageAdapter());
        viewPager.setCurrentItem(indexOf);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String path = allImagePath.get(position);
                judgeSelect(path);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //通过ViewPager实现滑动的图片
    private class listPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return allImagePath.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override

        public Object instantiateItem(ViewGroup container, final int position) {
            int i = position % 4;
            final LargeImageView pic = imageViews.get(i);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(pic,params);
            String path = allImagePath.get(position);
            pic.setImage(new FileBitmapDecoderFactory(new File(path)));
            return pic;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int i = position % 4;
            final LargeImageView imageView = imageViews.get(i);
            container.removeView(imageView);
        }
    }

    @Override
    public void finish() {
        super.finish();
        viewPager = null;
        overridePendingTransition(0, R.anim.pick_finish_slide_out_left);
    }

    //固定 toolbar
    private void hideOrShowToolbar() {
        myToolbar.animate()
                .translationY(mIsHidden ? 0 : -myToolbar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

    private void judgeSelect(final String path){
        int indexOf = selectImagePath.indexOf(path);
        if(indexOf != -1){
            myToolbar.setRightIconDefault(R.mipmap.pick_ic_select);
            misSelect = true;
        }else {
            myToolbar.setRightIcon(R.mipmap.pick_ic_un_select_black);
            misSelect = false;
        }

        myToolbar.setRightLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(misSelect){
                    myToolbar.setRightIcon(R.mipmap.pick_ic_un_select_black);
                    selectImagePath.remove(path);
                    misSelect = false;
                }else {
                    myToolbar.setRightIconDefault(R.mipmap.pick_ic_select);
                    selectImagePath.add(path);
                    misSelect = true;
                }
            }
        });
    }

    private void finishForResult(){
        Intent intent = new Intent();
        intent.setClass(PickPhotoPreviewActivity.this, PickPhotoActivity.class);
        intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, (Serializable) selectImagePath);
        setResult(PickConfig.PREVIEW_PHOTO_DATA,intent);
        finish();
    }
}
