package com.werb.pickphotoview

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.werb.library.MoreAdapter
import com.werb.pickphotoview.adapter.PickListAdapter
import com.werb.pickphotoview.extensions.color
import com.werb.pickphotoview.model.PickModel
import com.werb.pickphotoview.util.PickConfig

/**
 * Created by wanbo on 2017/1/3.
 */

class PickListActivity : AppCompatActivity() {

    private val model: PickModel? = GlobalData.model
    private val adapter: MoreAdapter by lazy { MoreAdapter() }

    private val listener = View.OnClickListener { v ->
        val dirName = v.getTag(R.id.pick_dir_name) as String
        val intent = Intent()
        intent.setClass(this@PickListActivity, PickPhotoActivity::class.java)
        intent.putExtra(PickConfig.INTENT_DIR_NAME, dirName)
        this@PickListActivity.setResult(PickConfig.LIST_PHOTO_DATA, intent)
        this@PickListActivity.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pick_activity_pick_photo)
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        model?.let {
            val window = window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = color(it.statusBarColor)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!it.lightStatusBar) {
                    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }

    private fun initRecyclerView() {
        val listAdapter = PickListAdapter(this@PickListActivity, listener)
    }

    override fun finish() {
        super.finish()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        if (intent.component!!.className == PickPhotoActivity::class.java.name) {
        }
    }
}
