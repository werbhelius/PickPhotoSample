package com.werb.pickphotoview.adapter

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import com.werb.library.MoreViewHolder
import com.werb.pickphotoview.GlobalData
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickUtils
import java.io.IOException

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/19. */

class CameraViewHolder(containerView: View) : MoreViewHolder<String>(containerView) {

    private val context = containerView.context

    init {
        GlobalData.model?.let {
            val screenWidth = PickUtils.getInstance(context).widthPixels
            val space = PickUtils.getInstance(context).dp2px(PickConfig.ITEM_SPACE.toFloat())
            val scaleSize = (screenWidth - (it.spanCount + 1) * space) / it.spanCount
            val params = containerView.layoutParams
            params.width = scaleSize
            params.height = scaleSize
        }
    }

    override fun bindData(data: String, payloads: List<Any>) {
        containerView.setOnClickListener {
            try {
                val photoFile = PickUtils.getInstance(context).getPhotoFile(context)
                photoFile.delete()
                if (photoFile.createNewFile()) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, PickUtils.getInstance(context).getUri(photoFile))
                    if (context is Activity) {
                        context.startActivityForResult(intent, PickConfig.CAMERA_PHOTO_DATA)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}