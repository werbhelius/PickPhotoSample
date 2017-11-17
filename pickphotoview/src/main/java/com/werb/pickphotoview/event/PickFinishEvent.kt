package com.werb.pickphotoview.event

import com.werb.eventbus.IEvent
import com.werb.pickphotoview.model.DirImage
import com.werb.pickphotoview.model.GroupImage
import com.werb.pickphotoview.util.PickConfig

/** Created by wanbo <werbhelius@gmail.com> on 2017/9/17. */

class PickFinishEvent(val dirName: String = PickConfig.ALL_PHOTOS) : IEvent