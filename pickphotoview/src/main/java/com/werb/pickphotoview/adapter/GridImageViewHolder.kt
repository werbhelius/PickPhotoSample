package com.werb.pickphotoview.adapter

import android.app.Activity
import android.graphics.PorterDuff
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.werb.library.MoreViewHolder
import com.werb.pickphotoview.GlobalData
import com.werb.pickphotoview.model.GridImage
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickUtils
import kotlinx.android.synthetic.main.pick_item_grid_layout.*
import com.bumptech.glide.Glide
import com.werb.pickphotoview.PickPhotoPreviewActivity
import com.werb.pickphotoview.R
import com.werb.pickphotoview.extensions.color
import com.werb.pickphotoview.extensions.drawable
import com.werb.pickphotoview.model.PickModel
import com.werb.pickphotoview.util.GlideHelper
import java.lang.ref.WeakReference


/** Created by wanbo <werbhelius@gmail.com> on 2017/9/17. */

class GridImageViewHolder(containerView: View) : MoreViewHolder<GridImage>(containerView) {

    private val context = containerView.context
    private var weekImage: ImageView? = null

    init {
        GlobalData.model?.let {
            val screenWidth = PickUtils.getInstance(context.applicationContext).widthPixels
            val space = PickUtils.getInstance(context.applicationContext).dp2px(PickConfig.ITEM_SPACE.toFloat())
            val scaleSize = (screenWidth - (it.spanCount + 1) * space) / it.spanCount
            val params = image.layoutParams
            params.width = scaleSize
            params.height = scaleSize

            val imageViewWeakReference = WeakReference<ImageView>(image)
            weekImage = imageViewWeakReference.get()
        }
    }

    override fun bindData(data: GridImage, payloads: List<Any>) {
        Glide.with(context)
                .asBitmap()
                .load(Uri.parse("file://" + data.path))
                .apply(GlideHelper.imageLoadOption())
                .into(weekImage)

        if (payloads.isNotEmpty()) {
            payloads.forEach {
                if (it is GridImage) {
                    select(it)
                }
            }
        } else {
            select(data)
        }

        if (data.path.endsWith(".gif")) {
            gif.visibility = View.VISIBLE
        } else {
            gif.visibility = View.GONE
        }

        GlobalData.model?.let {
            if (it.isClickSelectable && it.pickPhotoSize == 1) {
                selectLayout.tag = data
                containerView.tag = data
                addOnClickListener(selectLayout)
                addOnClickListener(containerView)
            } else {
                selectLayout.tag = data
                addOnClickListener(selectLayout)
                containerView.setOnClickListener {
                    PickPhotoPreviewActivity.startActivity(context as Activity, PickConfig.PREVIEW_PHOTO_DATA, data.path, data.dir)
                }
            }
        }
    }

    override fun unBindData() {
        Glide.with(context).clear(weekImage)
    }

    private fun select(data: GridImage) {
        if (data.select) {
            check.visibility = View.VISIBLE
            selectBack.visibility = View.VISIBLE
            val drawable = context.drawable(R.drawable.pick_svg_select_select)
            val back = context.drawable(R.drawable.pick_svg_select_back)
            GlobalData.model?.selectIconColor?.let {
                back.setColorFilter(context.color(it), PorterDuff.Mode.SRC_IN)
            }
            selectLayout.setBackgroundDrawable(drawable)
            selectBack.setBackgroundDrawable(back)
        } else {
            check.visibility = View.GONE
            selectBack.visibility = View.GONE
            val drawable = context.drawable(R.drawable.pick_svg_select_default)
            selectLayout.setBackgroundDrawable(drawable)
        }
    }

}