package com.werb.pickphotoview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.werb.pickphotoview.util.PickConfig;
import com.werb.pickphotoview.util.PickUtils;

import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by wanbo on 2017/1/4.
 */

public class PickPhotoPreviewActivity extends AppCompatActivity {

    private List<String> allImagePath;
    private String path;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_preview_photo);
        path = getIntent().getStringExtra(PickConfig.INTENT_IMG_PATH);
        allImagePath = (List<String>) getIntent().getSerializableExtra(PickConfig.INTENT_IMG_LIST);
        initView();
        Log.d("image size",allImagePath.size() + "");
    }

    private void initView(){
        viewPager = (ViewPager) findViewById(R.id.image_vp);
        int indexOf = allImagePath.indexOf(path);
        viewPager.setCurrentItem(indexOf);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(new listPageAdapter());
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

        public Object instantiateItem(ViewGroup container, int position) {
            ImageView pic = new ImageView(PickPhotoPreviewActivity.this);
            String path = allImagePath.get(position);
            Bitmap bitmap = PickUtils.loadBitmap(path, 100, 100);
            pic.setImageBitmap(bitmap);
            container.addView(pic);
            return pic;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void finish() {
        super.finish();
        viewPager = null;
        overridePendingTransition(0,R.anim.finish_slide_out_left);
    }


}
