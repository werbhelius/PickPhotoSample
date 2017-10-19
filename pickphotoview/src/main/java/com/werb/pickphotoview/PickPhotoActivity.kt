package com.werb.pickphotoview

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.werb.eventbus.EventBus
import com.werb.eventbus.Subscriber
import com.werb.pickphotoview.event.PickImageEvent
import com.werb.pickphotoview.extensions.alphaColor
import com.werb.pickphotoview.extensions.color
import com.werb.pickphotoview.extensions.drawable
import com.werb.pickphotoview.extensions.string
import com.werb.pickphotoview.ui.GridFragment
import com.werb.pickphotoview.ui.ListFragment
import com.werb.pickphotoview.util.*
import kotlinx.android.synthetic.main.pick_activity_pick_photo.*
import kotlinx.android.synthetic.main.pick_widget_my_toolbar.*
import kotlinx.android.synthetic.main.pick_widget_select_layout.*
import java.io.Serializable


/**
 * Created by wanbo on 2016/12/30.
 */

class PickPhotoActivity : AppCompatActivity() {

    private val mode = PickConfig.PICK_GIRD
    private val selectImages = PickPhotoHelper.selectImages

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pick_activity_pick_photo)
        initToolbar()
    }

    override fun onStart() {
        super.onStart()
        EventBus.register(this)
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.unRegister(this)
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
        val select = drawable(R.drawable.pick_svg_select)
        GlobalData.model?.let {
            val window = window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = color(it.statusBarColor)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (it.lightStatusBar) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
            toolbar.setBackgroundColor(color(it.toolbarColor))
            midTitle.setTextColor(color(it.toolbarTextColor))
            cancel.setTextColor(color(it.toolbarTextColor))
            sure.setTextColor(alphaColor(color(it.toolbarTextColor)))
            midTitle.text = string(R.string.pick_all_photo)
            sure.text = String.format(string(R.string.pick_photo_sure), selectImages.size)
            sure.setOnClickListener { add() }

            if (it.lightStatusBar) {
                select.setColorFilter(color(R.color.pick_gray), PorterDuff.Mode.SRC_IN)
            } else {
                select.setColorFilter(color(R.color.pick_white), PorterDuff.Mode.SRC_IN)
            }
            selectArrow.setBackgroundDrawable(select)

            cancel.setOnClickListener { finish() }

            showFragment()
        }
    }

    private fun showFragment() {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        val gridFragment = GridFragment.newInstance()
        val listFragment = ListFragment.newInstance()
        if (supportFragmentManager.findFragmentByTag(gridFragment::class.java.simpleName) == null) {
            transaction.add(R.id.content, gridFragment, GridFragment::class.java.simpleName)
        }
        if (supportFragmentManager.findFragmentByTag(listFragment::class.java.simpleName) == null) {
            transaction.add(R.id.content, listFragment, ListFragment::class.java.simpleName)
        }
        when (mode) {
            PickConfig.PICK_GIRD -> {
                transaction.show(gridFragment).hide(listFragment).commit()
            }
            PickConfig.PICK_LIST -> {
                transaction.show(listFragment).hide(gridFragment).commit()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == 0) {
//            return
//        }
//        if (requestCode == PickConfig.LIST_PHOTO_DATA) {
//            if (data != null) {
//                val dirName = data.getStringExtra(PickConfig.INTENT_DIR_NAME)
//                val listImage = PickPreferences.getInstance(this@PickPhotoActivity).listImage
//                allPhotos = listImage!!.mGroupMap[dirName]
//                pickGridAdapter!!.updateData(allPhotos)
//                toolbar.setPhotoDirName(dirName)
//                tv_pick_photo.text = getString(R.string.pick_pick)
//                tv_pick_photo.setTextColor(ContextCompat.getColor(this, R.color.pick_black))
//            }
//        } else if (requestCode == PickConfig.CAMERA_PHOTO_DATA) {
//            var path: String?
//            if (data != null) {
//                path = data.data.path
//                if (path!!.contains("/pick_camera")) {
//                    path = path.replace("/pick_camera", "/storage/emulated/0/DCIM/Camera")
//                }
//            } else {
//                path = PickUtils.getInstance(this@PickPhotoActivity).getFilePath(this@PickPhotoActivity)
//            }
//            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path!!)))
//            val intent = Intent()
//            val list = ArrayList<String>()
//            list.add(path)
//            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, list)
//            setResult(PickConfig.PICK_PHOTO_DATA, intent)
//            finish()
//        } else if (requestCode == PickConfig.PREVIEW_PHOTO_DATA) {
//            if (data != null) {
//                val selectPath = data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT) as ArrayList<String>
//                pickGridAdapter!!.selectPath = selectPath
//                pickGridAdapter!!.notifyDataSetChanged()
//            }
//        }
//    }

//    internal var imageClick: View.OnClickListener = View.OnClickListener { v ->
//        val imgPath = v.getTag(R.id.pick_image_path) as String
//        val intent = Intent()
//        intent.setClass(this@PickPhotoActivity, PickPhotoPreviewActivity::class.java)
//        intent.putExtra(PickConfig.INTENT_IMG_PATH, imgPath)
//        intent.putExtra(PickConfig.INTENT_IMG_LIST, allPhotos)
//        intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, pickGridAdapter!!.selectPath)
//        intent.putExtra(PickConfig.INTENT_PICK_DATA, GlobalData.model)
//        startActivityForResult(intent, PickConfig.PREVIEW_PHOTO_DATA)
//    }
//
//    private val selectClick = View.OnClickListener { select() }

    private fun add() {
        if (selectImages.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, selectImages as Serializable)
            setResult(PickConfig.PICK_PHOTO_DATA, intent)
            finish()
        }
    }

    @Subscriber()
    private fun textChange(event: PickImageEvent) {
        GlobalData.model?.let {
            if (selectImages.isEmpty()) {
                sure.setTextColor(alphaColor(color(it.toolbarTextColor)))
            } else {
                sure.setTextColor(color(it.toolbarTextColor))
            }
            sure.text = String.format(string(R.string.pick_photo_sure), selectImages.size)
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
