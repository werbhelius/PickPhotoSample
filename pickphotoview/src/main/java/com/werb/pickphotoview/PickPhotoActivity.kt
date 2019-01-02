package com.werb.pickphotoview

import android.app.Activity
import android.app.FragmentTransaction
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.werb.eventbus.EventBus
import com.werb.eventbus.Subscriber
import com.werb.pickphotoview.event.PickFinishEvent
import com.werb.pickphotoview.event.PickImageEvent
import com.werb.pickphotoview.extensions.alphaColor
import com.werb.pickphotoview.extensions.color
import com.werb.pickphotoview.extensions.drawable
import com.werb.pickphotoview.extensions.string
import com.werb.pickphotoview.ui.GridFragment
import com.werb.pickphotoview.ui.ListFragment
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickPhotoHelper
import com.werb.pickphotoview.util.PickUtils
import kotlinx.android.synthetic.main.pick_widget_my_toolbar.*
import java.io.Serializable


/**
 * Created by wanbo on 2016/12/30.
 */

class PickPhotoActivity :  BasePickActivity() {

    private var mode = PickConfig.PICK_GIRD
    private val selectImages = PickPhotoHelper.selectImages
    private val gridFragment = GridFragment()
    private val listFragment = ListFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.register(this)
        setContentView(R.layout.pick_activity_pick_photo)
        initToolbar()
    }

    private fun initToolbar() {
        val select = drawable(R.drawable.pick_svg_select)
        GlobalData.model?.let {
            toolbar.setBackgroundColor(color(it.toolbarColor))
            statusBar.setBackgroundColor(color(it.statusBarColor))
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
            switchLayout.setOnClickListener { switch() }

            showFragment()
        }
    }

    private fun showFragment() {
        val fm = supportFragmentManager
        val transaction = fm.beginTransaction()
        if (supportFragmentManager.findFragmentByTag(gridFragment::class.java.simpleName) == null) {
            transaction.add(R.id.content, gridFragment, GridFragment::class.java.simpleName)
        }
        if (supportFragmentManager.findFragmentByTag(listFragment::class.java.simpleName) == null) {
            transaction.add(R.id.content, listFragment, ListFragment::class.java.simpleName)
        }
        when (mode) {
            PickConfig.PICK_GIRD -> {
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                        .show(gridFragment).hide(listFragment).commit()
            }
            PickConfig.PICK_LIST -> {
                transaction
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .show(listFragment).hide(gridFragment).commit()
            }
        }
    }

    private fun switch() {
        when (mode) {
            PickConfig.PICK_GIRD -> {
                mode = PickConfig.PICK_LIST
                selectArrow.rotation = 180f
            }
            PickConfig.PICK_LIST -> {
                mode = PickConfig.PICK_GIRD
                selectArrow.rotation = 0f
            }
        }
        showFragment()
    }

    private fun add() {
        if (selectImages.isNotEmpty()) {
            val intent = Intent()
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, selectImages as Serializable)
            setResult(PickConfig.PICK_PHOTO_DATA, intent)
            finish()
        }
    }

    private fun sure(){
        GlobalData.model?.let {
            if (selectImages.isEmpty()) {
                sure.setTextColor(alphaColor(color(it.toolbarTextColor)))
            } else {
                sure.setTextColor(color(it.toolbarTextColor))
            }
            sure.text = String.format(string(R.string.pick_photo_sure), selectImages.size)
        }
    }

    @Subscriber()
    private fun textChange(event: PickImageEvent) {
       sure()
    }

    @Subscriber(tag = "switch")
    private fun pick(event: PickFinishEvent) {
        GlobalData.model?.let {
            switch()
            midTitle.text = event.dirName
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            return
        }
        if (requestCode == PickConfig.CAMERA_PHOTO_DATA) {
            var path: String?
            if (data != null && data.data != null) {
                path = data.data.path
                if (path.contains("/pick_camera")) {
                    path = path.replace("/pick_camera", "/storage/emulated/0/DCIM/Camera")
                }
            } else {
                path = PickUtils.getInstance(this.applicationContext).getFilePath(this)
            }
            sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path!!)))
            val intent = Intent()
            intent.putExtra(PickConfig.INTENT_IMG_LIST_SELECT, arrayListOf(path))
            setResult(PickConfig.PICK_PHOTO_DATA, intent)
            finish()
        }
        if (requestCode == PickConfig.PREVIEW_PHOTO_DATA) {
            add()
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
        EventBus.unRegister(this)
        PickPhotoHelper.stop()
        setContentView(R.layout.pick_null_layout)
    }

}
