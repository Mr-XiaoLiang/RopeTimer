package com.lollipop.ropetimer.protocol

import android.graphics.Color
import com.lollipop.ropetimer.Protocol

class NormalProtocol : Protocol {

    var name: String = ""
    val coverList = ArrayList<Cover>()
    val scaleList = ArrayList<Scale>()
    var themeColor: Int = Color.GREEN

}