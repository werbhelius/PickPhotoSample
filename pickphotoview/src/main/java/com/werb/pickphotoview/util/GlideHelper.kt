package com.werb.pickphotoview.util

import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.werb.pickphotoview.R


/** Created by wanbo <werbhelius@gmail.com> on 2017/10/15. */

object GlideHelper {

    /** Image load logic */
    fun imageLoadOption(): RequestOptions {
        return RequestOptions()
                .centerCrop()
                .placeholder(R.color.pick_placeholder)
                .error(R.color.pick_placeholder)
                .priority(Priority.LOW)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
    }

}