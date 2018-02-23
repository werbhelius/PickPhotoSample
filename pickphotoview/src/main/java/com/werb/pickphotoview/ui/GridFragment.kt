package com.werb.pickphotoview.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.werb.eventbus.EventBus
import com.werb.eventbus.Subscriber
import com.werb.eventbus.ThreadMode
import com.werb.library.MoreAdapter
import com.werb.library.action.MoreClickListener
import com.werb.library.link.RegisterItem
import com.werb.pickphotoview.GlobalData
import com.werb.pickphotoview.R
import com.werb.pickphotoview.adapter.CameraViewHolder
import com.werb.pickphotoview.adapter.GridImageViewHolder
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import com.werb.pickphotoview.event.PickFinishEvent
import com.werb.pickphotoview.event.PickImageEvent
import com.werb.pickphotoview.event.PickPreviewEvent
import com.werb.pickphotoview.extensions.string
import com.werb.pickphotoview.model.GridImage
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickPhotoHelper
import com.werb.pickphotoview.util.PickUtils
import kotlinx.android.synthetic.main.pick_fragment_grid.*
import java.io.Serializable

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/18. */

class GridFragment : Fragment() {

    private lateinit var adapter: MoreAdapter
    private val manager: RequestManager by lazy { Glide.with(this) }
    private val selectImages = PickPhotoHelper.selectImages

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("GridFragment create " + this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pick_fragment_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        EventBus.register(this)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.unRegister(this)
    }

    private fun initView() {
        GlobalData.model?.let {
            recyclerView.addItemDecoration(SpaceItemDecoration(PickUtils.getInstance(context?.applicationContext).dp2px(PickConfig.ITEM_SPACE.toFloat()), it.spanCount))
            recyclerView.layoutManager = GridLayoutManager(context, it.spanCount)
            recyclerView.addOnScrollListener(scrollListener)
            adapter = MoreAdapter()
            adapter.apply {
                register(RegisterItem(R.layout.pick_item_grid_layout, GridImageViewHolder::class.java, selectListener))
                register(RegisterItem(R.layout.pick_item_camera_layout, CameraViewHolder::class.java))
                attachTo(recyclerView)
            }
        }
    }

    /** image select listener */
    private val selectListener = object : MoreClickListener() {
        override fun onItemClick(view: View, position: Int) {
            //add to list
            GlobalData.model?.let {
                val gridImage = view.tag as GridImage
                val data = adapter.getData(position) as GridImage
                if (gridImage.select) {
                    removeImage(data, position)
                } else {
                    if (it.allPhotoSize != 0) {
                        val pickSize = selectImages.size + it.hasPhotoSize
                        if (pickSize >= it.allPhotoSize) {
                            context?.let {c ->
                                Toast.makeText(c, String.format(c.string(R.string.pick_photo_size_limit), it.allPhotoSize.toString()), Toast.LENGTH_SHORT).show()
                            }
                            return
                        }
                    }
                    val pickSize = selectImages.size
                    if (pickSize >= it.pickPhotoSize) {
                        context?.let {c ->
                            Toast.makeText(c, String.format(c.string(R.string.pick_photo_size_limit), it.allPhotoSize.toString()), Toast.LENGTH_SHORT).show()
                        }
                        return
                    } else {
                        addImage(data, position)
                        if (it.isClickSelectable && it.pickPhotoSize == 1) {
                            add()
                        }
                    }
                }
            }
        }
    }

    /** add image in list */
    private fun addImage(image: GridImage, position: Int) {
        image.select = true
        selectImages.add(image.path)
        adapter.notifyItemChanged(position, image)
        EventBus.post(PickImageEvent())
    }

    /** remove image in list */
    private fun removeImage(image: GridImage, position: Int) {
        image.select = false
        selectImages.remove(image.path)
        adapter.notifyItemChanged(position, image)
        EventBus.post(PickImageEvent())
    }

    private fun add() {
        if (selectImages.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, selectImages as Serializable)
            activity?.setResult(PickConfig.PICK_PHOTO_DATA, intent)
            activity?.finish()
        }
    }

    /** load image into RecyclerView */
    @Subscriber(mode = ThreadMode.MAIN)
    private fun images(event: PickFinishEvent) {
        adapter.removeAllData()
        GlobalData.model?.let {
            if (it.isShowCamera) {
                adapter.loadData("camera")
            }
        }

        val groupImage = PickPhotoHelper.groupImage
        val allPhotos = groupImage?.mGroupMap?.get(event.dirName)
        if (allPhotos == null) {
            Log.d("PickPhotoView", "Image is Empty")
        } else {
            Log.d("All photos size:", allPhotos.size.toString())
            allPhotos.forEach {
                adapter.loadData(GridImage(it, selectImages.contains(it), event.dirName))
            }
        }
    }

    @Subscriber(mode = ThreadMode.MAIN, tag = "switch")
    private fun switch(event: PickFinishEvent) {
        images(event)
    }

    @Subscriber()
    private fun select(event: PickPreviewEvent) {
        adapter.list.forEach {
            if (it is GridImage) {
                if (it.path == event.path) {
                    it.select = selectImages.contains(event.path)
                    adapter.notifyItemChanged(adapter.list.indexOf(it), it)
                }
            }
        }
        EventBus.post(PickImageEvent())
    }

    /** image load pause when recyclerView scroll quickly */
    private var scrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (Math.abs(dy) > PickConfig.SCROLL_THRESHOLD) {
                manager.pauseRequests()
            } else {
                manager.resumeRequests()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                manager.resumeRequests()
            }
        }
    }

}