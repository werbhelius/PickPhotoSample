package com.werb.pickphotoview;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickUtils;
import com.werb.pickphotoview.widget.MyToolbar;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wanbo on 2017/1/4.
 */

public class PickPhotoPreviewActivity extends AppCompatActivity {

    private List<String> allImagePath;
    private String path;
    private ViewPager viewPager;
    private List<ImageView> imageViews;
    private MyToolbar myToolbar;
    private boolean mIsHidden;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_activty_preview_photo);
        path = getIntent().getStringExtra(PickConfig.INTENT_IMG_PATH);
        allImagePath = (List<String>) getIntent().getSerializableExtra(PickConfig.INTENT_IMG_LIST);
        imageViews = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ImageView imageView = new ImageView(this);
            imageViews.add(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideOrShowToolbar();
                }
            });
        }
        initView();
        Log.d("image size", allImagePath.size() + "");
    }

    private void initView() {
        myToolbar = (MyToolbar) findViewById(R.id.toolbar);
        myToolbar.setLeftIcon(R.mipmap.pick_ic_back);
        myToolbar.setLeftLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.image_vp);
        int indexOf = allImagePath.indexOf(path);
        viewPager.setAdapter(new listPageAdapter());
        viewPager.setCurrentItem(indexOf);
    }

    //通过ViewPager实现滑动的图片
    class listPageAdapter extends PagerAdapter {

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
            final ImageView pic = imageViews.get(i);
            container.addView(pic);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String path = allImagePath.get(position);
                    final Bitmap bitmap = PickUtils.getInstance(PickPhotoPreviewActivity.this).loadBitmap(path, 1000, 1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pic.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
            return pic;
        }

        Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            int i = position % 4;
            final ImageView imageView = imageViews.get(i);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
            if (bitmapDrawable != null) {
                Bitmap bm = bitmapDrawable.getBitmap();
                if (bm != null && !bm.isRecycled()) {
                    Log.d("...desimg..", "被回收了" + bm.getByteCount());
                    imageView.setImageResource(0);
                    bm.recycle();
                }
            }
            container.removeView(imageView);
        }
    }

    @Override
    public void finish() {
        super.finish();
        viewPager = null;
        overridePendingTransition(0, R.anim.pick_finish_slide_out_left);
    }

    private void hideOrShowToolbar() {
        myToolbar.animate()
                .translationY(mIsHidden ? 0 : -myToolbar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }
}
