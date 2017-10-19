package com.werb.pickphotoview.ui

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
import com.werb.pickphotoview.adapter.GridImageViewHolder
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import com.werb.pickphotoview.event.PickFinishEvent
import com.werb.pickphotoview.event.PickImageEvent
import com.werb.pickphotoview.extensions.string
import com.werb.pickphotoview.model.GridImage
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickPhotoHelper
import com.werb.pickphotoview.util.PickPreferences
import com.werb.pickphotoview.util.PickUtils
import kotlinx.android.synthetic.main.pick_fragment_grid.*

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/18. */

class GridFragment : Fragment() {

    private val adapter: MoreAdapter by lazy { MoreAdapter() }
    private val manager: RequestManager by lazy { Glide.with(this) }
    private val selectImages = PickPhotoHelper.selectImages

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.pick_fragment_grid, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        EventBus.register(this)
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.unRegister(this)
    }

    private fun initView() {
        GlobalData.model?.let {
            recyclerView.addItemDecoration(SpaceItemDecoration(PickUtils.getInstance(context).dp2px(PickConfig.ITEM_SPACE.toFloat()), it.spanCount))
            recyclerView.layoutManager = GridLayoutManager(context, it.spanCount)
            recyclerView.addOnScrollListener(scrollListener)

            adapter.apply {
                register(RegisterItem(R.layout.pick_item_grid_layout, GridImageViewHolder::class.java, selectListener))
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
                    if (selectImages.size >= it.pickPhotoSize) {
                        Toast.makeText(context, String.format(context.string(R.string.pick_photo_size_limit), it.pickPhotoSize.toString()), Toast.LENGTH_SHORT).show()
                        return
                    } else {
                        addImage(data, position)
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

    /** load image into RecyclerView */
    @Subscriber(mode = ThreadMode.MAIN)
    private fun images(event: PickFinishEvent) {
        val groupImage = PickPreferences.getInstance(context).listImage
        val allPhotos = groupImage.mGroupMap[PickConfig.ALL_PHOTOS]
        if (allPhotos == null) {
            Log.d("PickPhotoView", "Image is Empty")
        } else {
            Log.d("All photos size:", allPhotos.size.toString())
            allPhotos.forEach {
                adapter.loadData(GridImage(it, false))
            }
        }
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

    companion object {

        fun newInstance(): GridFragment = GridFragment()

    }

}