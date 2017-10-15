package com.werb.pickphotoview.adapter

import android.net.Uri
import android.view.View
import com.werb.library.MoreViewHolder
import com.werb.pickphotoview.GlobalData
import com.werb.pickphotoview.model.GridImage
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickUtils
import kotlinx.android.synthetic.main.pick_item_grid_layout.*
import com.bumptech.glide.Glide
import com.werb.pickphotoview.util.GlideHelper


/** Created by wanbo <werbhelius@gmail.com> on 2017/9/17. */

class GridImageViewHolder(containerView: View) : MoreViewHolder<GridImage>(containerView) {

    private val context = containerView.context

    init {
        GlobalData.model?.let {
            val screenWidth = PickUtils.getInstance(context).widthPixels
            val space = PickUtils.getInstance(context).dp2px(PickConfig.ITEM_SPACE.toFloat())
            val scaleSize = (screenWidth - (it.spanCount + 1) * space) / it.spanCount
            val params = image.layoutParams
            params.width = scaleSize
            params.height = scaleSize
        }
    }

    override fun bindData(data: GridImage) {
        Glide.with(context)
                .load(Uri.parse("file://" + data.path))
                .apply(GlideHelper.imageLoadOption())
                .thumbnail(0.3f)
                .into(image)
        select.isChecked = data.select
    }

    override fun unBindData() {
        Glide.with(context).clear(image)
    }

}