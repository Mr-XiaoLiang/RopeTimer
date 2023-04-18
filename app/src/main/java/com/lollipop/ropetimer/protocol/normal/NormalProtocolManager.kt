package com.lollipop.ropetimer.protocol.normal

import android.content.Context
import android.graphics.Color
import com.lollipop.ropetimer.protocol.FileCover
import com.lollipop.ropetimer.protocol.NormalProtocol
import com.lollipop.ropetimer.protocol.Scale
import com.lollipop.ropetimer.protocol.base.ProtocolManager

class NormalProtocolManager : ProtocolManager() {

    companion object {
        private const val KEY_COLOR = "color"
        private const val KEY_SCALE = "scale"
        private const val KEY_COVER = "cover"
    }

    override val protocolDirName: String = "normal"

    fun remove(protocol: NormalProtocol) {
        removeProtocolFile(protocol)
    }

    fun loadProtocol(name: String, context: Context, callback: (NormalProtocol) -> Unit) {
        loadInfo(name) { json ->
            val scaleJson = json.optJSONArray(KEY_SCALE)
            val coverJson = json.optJSONArray(KEY_COVER)
            val colorValue = json.optString(KEY_COLOR)
            val protocol = NormalProtocol()
            protocol.name = name
            protocol.themeColor = parseColor(colorValue) ?: Color.GREEN
            scaleJson?.let { array ->
                val length = array.length()
                for (i in 0 until length) {
                    val scale = array.optJSONObject(i) ?: continue
                    protocol.scaleList.add(Scale().apply { fromJson(scale) })
                }
            }
            coverJson?.let { array ->
                val length = array.length()
                for (i in 0 until length) {
                    val cover = array.optJSONObject(i) ?: continue
                    val info = cover.toCover(context) ?: continue
                    protocol.coverList.add(info)
                }
            }
            callback(protocol)
        }
    }

    private fun removeProtocolFile(protocol: NormalProtocol) {
        val file = getProtocolFile(protocol.name)
        if (file.exists()) {
            file.delete()
        }
        protocol.coverList.forEach { cover ->
            if (cover is FileCover) {
                if (cover.file.exists()) {
                    cover.file.delete()
                }
            }
        }
    }

}