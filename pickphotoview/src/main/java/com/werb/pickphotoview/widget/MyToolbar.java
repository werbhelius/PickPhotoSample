package com.werb.pickphotoview.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.werb.pickphotoview.R;

/**
 * Created by wanbo on 2017/1/3.
 */

public class MyToolbar extends RelativeLayout {

    public FrameLayout leftLayout, rightLayout;
    private TextView photoDirName;
    private ImageView leftIcon, rightIcon;

    public MyToolbar(Context context) {
        super(context);
    }

    public MyToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        setBackgroundColor(getResources().getColor(R.color.pick_black));
        FrameLayout container = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.pick_widget_my_toolbar, null, false);
        leftLayout = (FrameLayout) container.findViewById(R.id.toolbar_left_layout);
        rightLayout = (FrameLayout) container.findViewById(R.id.toolbar_right_layout);
        photoDirName = (TextView) container.findViewById(R.id.tv_photo_type_name);
        leftIcon = (ImageView) container.findViewById(R.id.toolbar_left_icon);
        rightIcon = (ImageView) container.findViewById(R.id.toolbarc_right_icon);
        rightIcon.setColorFilter(getResources().getColor(R.color.pick_white));
        addView(container);
    }

    public void setPhotoDirName(String dirName){
        photoDirName.setText(dirName);
    }

    public void setLeftLayoutOnClickListener(View.OnClickListener clickListener){
        leftLayout.setOnClickListener(clickListener);
    }

    public void setRightLayoutOnClickListener(View.OnClickListener clickListener){
        rightLayout.setOnClickListener(clickListener);
    }

    public void setLeftIcon(int drawableId){
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setColorFilter(getResources().getColor(R.color.pick_white), PorterDuff.Mode.SRC_ATOP);
        leftIcon.setBackgroundDrawable(drawable);
    }

    public void setRightIcon(int drawableId){
        Drawable drawable = getResources().getDrawable(drawableId);
        drawable.setColorFilter(getResources().getColor(R.color.pick_white), PorterDuff.Mode.SRC_ATOP);
        rightIcon.setBackgroundDrawable(drawable);
    }

}
