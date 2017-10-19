package com.werb.pickphotoview.adapter

import android.net.Uri
import android.view.View
import com.bumptech.glide.Glide
import com.werb.eventbus.EventBus
import com.werb.library.MoreViewHolder
import com.werb.pickphotoview.event.PickFinishEvent
import com.werb.pickphotoview.model.GroupImage
import com.werb.pickphotoview.util.GlideHelper
import com.werb.pickphotoview.util.PickPreferences
import kotlinx.android.synthetic.main.pick_item_list_layout.*

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/18. */

class DirImageViewHolder(containerView: View) : MoreViewHolder<String>(containerView) {

    private val context = containerView.context
    private val groupImage: GroupImage = PickPreferences.getInstance(context).listImage

    override fun bindData(data: String, payloads: List<Any>) {
        val list = groupImage.mGroupMap?.get(data) as List<String>
        if (list.isNotEmpty()) {
            Glide.with(context)
                    .load(Uri.parse("file://" + list[0]))
                    .apply(GlideHelper.imageLoadOption())
                    .into(cover)
            photoSize.text = list.size.toString()
            dirName.text =  data
            containerView.setOnClickListener {
                EventBus.post(PickFinishEvent(data), "switch")
            }
        }
    }
}