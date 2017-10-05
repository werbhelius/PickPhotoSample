package com.werb.pickphotoview.widget;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.werb.pickphotoview.R;
import com.werb.pickphotoview.util.PickConfig;

/**
 * Created by wanbo on 2017/1/3.
 */

public class MyToolbar extends RelativeLayout {

    public FrameLayout leftLayout, rightLayout,container;
    private TextView photoDirName;
    private ImageView leftIcon, rightIcon;
    private View slider;
    private int iconColor, selectColor;
    private Context mContext;

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
        mContext = context;
        container = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.pick_widget_my_toolbar, null, false);
        leftLayout = container.findViewById(R.id.toolbar_left_layout);
        rightLayout = container.findViewById(R.id.toolbar_right_layout);
        photoDirName = container.findViewById(R.id.tv_photo_type_name);
        leftIcon = container.findViewById(R.id.toolbar_left_icon);
        rightIcon = container.findViewById(R.id.toolbarc_right_icon);
        rightIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.pick_white));
        slider = container.findViewById(R.id.bottom_slider);
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

    public FrameLayout getRightLayout() {
        return rightLayout;
    }

    public void setLeftIcon(int drawableId){
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            leftIcon.setBackground(drawable);
        }
        else
        {
            //noinspection deprecation
            leftIcon.setBackgroundDrawable(drawable);
        }
    }

    public void setRightIcon(int drawableId){
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        drawable.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rightIcon.setBackground(drawable);
        }
        else
        {
            //noinspection deprecation
            rightIcon.setBackgroundDrawable(drawable);
        }
    }

    public void setRightIconDefault(int drawableId){
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        drawable.clearColorFilter();
        drawable.setColorFilter(selectColor, PorterDuff.Mode.SRC_ATOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rightIcon.setBackground(drawable);
        }
        else
        {
            //noinspection deprecation
            rightIcon.setBackgroundDrawable(drawable);
        }
    }

    public void setBackgroundColor(int backgroundColor){
        container.setBackgroundColor(backgroundColor);
    }

    public void setIconColor(int iconColor){
        this.iconColor = iconColor;
        photoDirName.setTextColor(iconColor);
    }

    public void setSelectColor(int selectColor){
        this.selectColor = selectColor;
    }

    public void hideSlider(){
        slider.setVisibility(GONE);
    }

}
