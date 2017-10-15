package com.werb.pickphotoview

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.werb.eventbus.EventBus
import com.werb.eventbus.Subscriber
import com.werb.eventbus.ThreadMode
import com.werb.library.MoreAdapter
import com.werb.library.link.RegisterItem
import com.werb.pickphotoview.adapter.GridImageViewHolder
import com.werb.pickphotoview.adapter.PickGridAdapter
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import com.werb.pickphotoview.model.GridImage
import com.werb.pickphotoview.util.*
import kotlinx.android.synthetic.main.pick_activity_pick_photo.*
import kotlinx.android.synthetic.main.pick_widget_select_layout.*
import java.util.*


/**
 * Created by wanbo on 2016/12/30.
 */

class PickPhotoActivity : AppCompatActivity() {

    private var pickGridAdapter: PickGridAdapter? = null
    private var allPhotos: ArrayList<String>? = null
    private val adapter: MoreAdapter by lazy { MoreAdapter() }
    private val manager: RequestManager by lazy { Glide.with(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pick_activity_pick_photo)
        initToolbar()
        initRecyclerView()
        initSelectLayout()
    }

    override fun onStart() {
        super.onStart()
        EventBus.register(this)
        getData()
    }

    override fun onStop() {
        super.onStop()
        EventBus.unRegister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        PickPhotoHelper.stop()
    }

//    override fun finish() {
//        super.finish()
//        PickHolder.newInstance() //Reset stored selected image paths.
//        overridePendingTransition(0, R.anim.pick_finish_slide_out_bottom)
//    }

    private fun getData() {
        GlobalData.model?.let {
            PickPhotoHelper.start(it.isShowGif, this)
        }
    }

    private fun initToolbar() {
        GlobalData.model?.let {
            val window = window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = resources.getColor(it.statusBarColor)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!it.lightStatusBar) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
            tv_preview_photo.text = "0"
            toolbar.setBackgroundColor(resources.getColor(it.toolbarColor))
            toolbar.setIconColor(resources.getColor(it.toolbarIconColor))
            toolbar.setLeftIcon(R.mipmap.pick_ic_open)
            toolbar.setRightIcon(R.mipmap.pick_ic_close)
            toolbar.setPhotoDirName(getString(R.string.pick_all_photo))
            toolbar.setLeftLayoutOnClickListener { this@PickPhotoActivity.startPhotoListActivity() }
            toolbar.setRightLayoutOnClickListener { this@PickPhotoActivity.finish() }
            tv_pick_photo.setOnClickListener(selectClick)
        }
    }

    private fun initRecyclerView() {
        GlobalData.model?.let {
            recyclerView.itemAnimator = DefaultItemAnimator()
            recyclerView.addItemDecoration(SpaceItemDecoration(PickUtils.getInstance(this@PickPhotoActivity).dp2px(PickConfig.ITEM_SPACE.toFloat()), it.spanCount))
            recyclerView.layoutManager = GridLayoutManager(this, it.spanCount)
            recyclerView.addOnScrollListener(scrollListener)

            adapter.apply {
                register(RegisterItem(R.layout.pick_item_grid_layout, GridImageViewHolder::class.java))
                attachTo(recyclerView)
            }
        }

//        val helper = PickPhotoHelper(this@PickPhotoActivity, PickPhotoListener {
//            val groupImage = PickPreferences.getInstance(this@PickPhotoActivity).listImage
//            allPhotos = groupImage!!.mGroupMap[PickConfig.ALL_PHOTOS]
//            if (allPhotos == null) {
//                Log.d("PickPhotoView", "Image is Empty")
//            } else {
//                Log.d("All photos size:", allPhotos!!.size.toString())
//            }
//            if (allPhotos != null && !allPhotos!!.isEmpty()) {
//                pickGridAdapter = PickGridAdapter(this@PickPhotoActivity, allPhotos, pickData, imageClick)
//                photoList!!.adapter = pickGridAdapter
//            }
//        })
//        helper.getImages(pickData!!.isShowGif)
    }

    @Subscriber(mode = ThreadMode.MAIN)
    private fun images(event: PickFinishEvent) {
        val groupImage = PickPreferences.getInstance(this@PickPhotoActivity).listImage
        allPhotos = groupImage.mGroupMap[PickConfig.ALL_PHOTOS]
        if (allPhotos == null) {
            Log.d("PickPhotoView", "Image is Empty")
        } else {
            Log.d("All photos size:", allPhotos?.size.toString())
            allPhotos?.forEach {
                adapter.loadData(GridImage(it, false))
            }
        }
    }

    private fun initSelectLayout() {
        val selectLayout = findViewById<View>(R.id.select_layout) as LinearLayout
        selectLayout.visibility = View.VISIBLE
    }

    fun updateSelectText(selectSize: String) {
        if (selectSize == "0") {
            tv_preview_photo.text = 0.toString()
            tv_pick_photo.setTextColor(ContextCompat.getColor(this, R.color.pick_gray))
            tv_pick_photo.isEnabled = false
        } else {
            tv_preview_photo.text = selectSize
            tv_pick_photo.setTextColor(GlobalData.model!!.selectIconColor)
            tv_pick_photo.isEnabled = true
        }
    }

    private fun startPhotoListActivity() {
        val intent = Intent()
        intent.setClass(this@PickPhotoActivity, PickListActivity::class.java)
        intent.putExtra(PickConfig.INTENT_PICK_DATA, GlobalData.model)
        startActivityForResult(intent, PickConfig.LIST_PHOTO_DATA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            return
        }
        if (requestCode == PickConfig.LIST_PHOTO_DATA) {
            if (data != null) {
                val dirName = data.getStringExtra(PickConfig.INTENT_DIR_NAME)
                val listImage = PickPreferences.getInstance(this@PickPhotoActivity).listImage
                allPhotos = listImage!!.mGroupMap[dirName]
                pickGridAdapter!!.updateData(allPhotos)
                toolbar.setPhotoDirName(dirName)
                tv_pick_photo.text = getString(R.string.pick_pick)
                tv_pick_photo.setTextColor(ContextCompat.getColor(this, R.color.pick_black))
            }
        } else if (requestCode == PickConfig.CAMERA_PHOTO_DATA) {
            var path: String?
            if (data != null) {
                path = data.data.path
                if (path!!.contains("/pick_camera")) {
                    path = path.replace("/pick_camera", "/storage/emulated/0/DCIM/Camera")
                }
            } else {
                path = PickUtils.getInstance(this@PickPhotoActivity).getFilePath(this@PickPhotoActivity)
            }
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path!!)))
            val intent = Intent()
            val list = ArrayList<String>()
            list.add(path)
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, list)
            setResult(PickConfig.PICK_PHOTO_DATA, intent)
            finish()
        } else if (requestCode == PickConfig.PREVIEW_PHOTO_DATA) {
            if (data != null) {
                val selectPath = data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT) as ArrayList<String>
                pickGridAdapter!!.selectPath = selectPath
                pickGridAdapter!!.notifyDataSetChanged()
                updateSelectText(selectPath.size.toString())
            }
        }
    }

    internal var imageClick: View.OnClickListener = View.OnClickListener { v ->
        val imgPath = v.getTag(R.id.pick_image_path) as String
        val intent = Intent()
        intent.setClass(this@PickPhotoActivity, PickPhotoPreviewActivity::class.java)
        intent.putExtra(PickConfig.INTENT_IMG_PATH, imgPath)
        intent.putExtra(PickConfig.INTENT_IMG_LIST, allPhotos)
        intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, pickGridAdapter!!.selectPath)
        intent.putExtra(PickConfig.INTENT_PICK_DATA, GlobalData.model)
        startActivityForResult(intent, PickConfig.PREVIEW_PHOTO_DATA)
    }

    private val selectClick = View.OnClickListener { select() }

    fun select() {
        if (pickGridAdapter == null) {
            return
        }

        if (!pickGridAdapter!!.selectPath.isEmpty()) {
            val intent = Intent()
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, pickGridAdapter!!.selectPath)
            setResult(PickConfig.PICK_PHOTO_DATA, intent)
            finish()
        }
    }

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
        fun startActivity(activity: Activity, requestCode: Int) {
            val intent = Intent(activity, PickPhotoActivity::class.java)
            activity.startActivityForResult(intent, requestCode)
            activity.overridePendingTransition(R.anim.activity_anim_bottom_to_top, R.anim.activity_anim_not_change)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.activity_anim_not_change, R.anim.activity_anim_top_to_bottom)
    }

}
