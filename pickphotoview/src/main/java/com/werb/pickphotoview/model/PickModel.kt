package com.werb.pickphotoview.model

import com.werb.pickphotoview.util.PickConfig
import java.io.Serializable

/** Created by wanbo <werbhelius@gmail.com> on 2017/9/16. */

class PickModel : Serializable {

    var pickPhotoSize: Int = 0
    var hasPhotoSize: Int = 0
    var allPhotoSize: Int = 0
    var spanCount: Int = 0
    var isShowCamera = false
    var isClickSelectable = false
    var toolbarColor = PickConfig.PICK_WHITE_COLOR
    var statusBarColor = PickConfig.PICK_WHITE_COLOR
    var toolbarTextColor = PickConfig.PICK_BLACK_COLOR
    var selectIconColor = PickConfig.PICK_BLACK_COLOR
    var lightStatusBar = false
    var isShowGif = true
    var isShowVideo = true

}