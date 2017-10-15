package com.werb.pickphotoview.event

import com.werb.eventbus.IEvent
import com.werb.pickphotoview.model.GridImage

/** Created by wanbo <werbhelius@gmail.com> on 2017/10/15. */

data class PickImageEvent(val image: GridImage): IEvent