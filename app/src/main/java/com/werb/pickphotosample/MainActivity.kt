package com.werb.pickphotosample

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.werb.permissionschecker.PermissionChecker
import com.werb.pickphotoview.PickPhotoView
import com.werb.pickphotoview.adapter.SpaceItemDecoration
import com.werb.pickphotoview.util.PickConfig
import com.werb.pickphotoview.util.PickUtils

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var adapter: SampleAdapter? = null
    private var permissionChecker: PermissionChecker? = null
    private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionChecker = PermissionChecker(this)
        if (permissionChecker!!.isLackPermissions(PERMISSIONS)) {
            permissionChecker!!.requestPermissions()
        }

        //Select Single Image - When image is selected, gallery immediately closes and returns image.
        val btn1 = findViewById<AppCompatTextView>(R.id.btn1)
        btn1.setOnClickListener {
            PickPhotoView.Builder(this@MainActivity)
                .setPickPhotoSize(1)                  // select image size
                .setClickSelectable(true)             // click one image immediately close and return image
                .setShowCamera(true)                  // is show camera
                .setSpanCount(3)                      // span count
                .setLightStatusBar(true)              // lightStatusBar used in Android M or higher
                .setStatusBarColor(R.color.white)     // statusBar color
                .setToolbarColor(R.color.white)       // toolbar color
                .setToolbarTextColor(R.color.black)   // toolbar text color
                .setSelectIconColor(R.color.pink)     // select icon color
                .setShowGif(false)                    // is show gif
                .start()
        }

        //Select Multiple Images - User can select multiple images and click Select to confirm.
        val btn2 = findViewById<AppCompatTextView>(R.id.btn2)
        btn2.setOnClickListener {
            PickPhotoView.Builder(this@MainActivity)
                .setPickPhotoSize(9)
                .setHasPhotoSize(7)
                .setAllPhotoSize(10)
                .setShowCamera(true)
                .setSpanCount(4)
                .setLightStatusBar(false)
                .setStatusBarColor(R.color.green_primary_dark)
                .setToolbarColor(R.color.green_primary)
                .setToolbarTextColor(R.color.white)
                .setSelectIconColor(R.color.green_primary)
                .start()
        }

        //Image Preview Select - Clicking on image opens Image Preview. Must click select icon to select image.
        val btn3 = findViewById<View>(R.id.btn3) as AppCompatTextView
        btn3.setOnClickListener {
            PickPhotoView.Builder(this@MainActivity)
                .setPickPhotoSize(6)
                .setShowCamera(true)
                .setSpanCount(4)
                .setLightStatusBar(false)
                .setStatusBarColor(R.color.blue_primary_dark)
                .setToolbarColor(R.color.blue_primary)
                .setToolbarTextColor(R.color.white)
                .setSelectIconColor(R.color.blue_primary)
                .start()
        }

        val photoList = findViewById<View>(R.id.photo_list) as RecyclerView
        val layoutManager = GridLayoutManager(this, 4)
        photoList.layoutManager = layoutManager
        photoList.addItemDecoration(SpaceItemDecoration(PickUtils.getInstance(this.applicationContext).dp2px(PickConfig.ITEM_SPACE.toFloat()), 4))
        adapter = SampleAdapter(this, null)
        photoList.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            return
        }
        if (data == null) {
            return
        }
        if (requestCode == PickConfig.PICK_PHOTO_DATA) {
            val selectPaths = data.getSerializableExtra(PickConfig.INTENT_IMG_LIST_SELECT) as ArrayList<String>
            adapter!!.updateData(selectPaths)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PermissionChecker.PERMISSION_REQUEST_CODE -> if (!permissionChecker!!.hasAllPermissionsGranted(grantResults)) {
                permissionChecker!!.showDialog()
            }
        }
    }
}
