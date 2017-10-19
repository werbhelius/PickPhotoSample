package com.werb.pickphotoview.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.werb.eventbus.EventBus
import com.werb.library.MoreAdapter
import com.werb.library.link.RegisterItem
import com.werb.pickphotoview.GlobalData
import com.werb.pickphotoview.R
import com.werb.pickphotoview.adapter.DirImageViewHolder
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickPreferences
import com.werb.pickphotoview.util.PickUtils
import kotlinx.android.synthetic.main.pick_fragment_grid.*

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/18. */

class ListFragment : Fragment() {

    private val adapter: MoreAdapter by lazy { MoreAdapter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.pick_fragment_grid, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initView()
        loadData()
    }

    private fun initView() {
        GlobalData.model?.let {
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter.apply {
                register(RegisterItem(R.layout.pick_item_list_layout, DirImageViewHolder::class.java))
                attachTo(recyclerView)
            }
        }
    }

    private fun loadData() {
        val dirImage = PickPreferences.getInstance(context).dirImage
        adapter.loadData(dirImage.dirName)
    }

    companion object {
        fun newInstance(): ListFragment = ListFragment()
    }

}