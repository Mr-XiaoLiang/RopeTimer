package com.lollipop.ropetimer.protocol

import org.json.JSONObject

open class Scale {

    companion object {
        const val KEY_MAX = "max"
    }

    var max: Int = 0
        protected set

    open fun toJson(): JSONObject {
        return JSONObject().put(KEY_MAX, max)
    }

    open fun fromJson(json: JSONObject) {
        max = json.optInt(KEY_MAX)
    }

}