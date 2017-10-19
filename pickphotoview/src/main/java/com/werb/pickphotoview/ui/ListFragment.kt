package com.werb.pickphotoview.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.werb.eventbus.EventBus
import com.werb.library.MoreAdapter
import com.werb.pickphotoview.GlobalData
import com.werb.pickphotoview.R
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickUtils
import kotlinx.android.synthetic.main.pick_fragment_grid.*

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/18. */

class ListFragment : Fragment() {

    private val adapter: MoreAdapter by lazy { MoreAdapter() }
    private val selectImages: MutableList<String> by lazy { mutableListOf<String>() }
    private val manager: RequestManager by lazy { Glide.with(this) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.pick_fragment_grid, container, false)
    }

    private fun initView() {
        GlobalData.model?.let {
            recyclerView.addItemDecoration(SpaceItemDecoration(PickUtils.getInstance(context).dp2px(PickConfig.ITEM_SPACE.toFloat()), it.spanCount))
            recyclerView.layoutManager = GridLayoutManager(context, it.spanCount)

            adapter.apply {
                attachTo(recyclerView)
            }
        }
    }

    companion object {
        fun newInstance(): ListFragment = ListFragment()
    }

}