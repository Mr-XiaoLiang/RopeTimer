package com.lollipop.ropetimer.protocol.base

import android.content.Context
import com.lollipop.ropetimer.BuildConfig
import com.lollipop.ropetimer.protocol.Cover
import com.lollipop.ropetimer.protocol.FileCover
import com.lollipop.ropetimer.protocol.ResourceCover
import com.lollipop.ropetimer.protocol.base.ProtocolManager.CoverType.FILE
import com.lollipop.ropetimer.protocol.base.ProtocolManager.CoverType.RESOURCE
import com.lollipop.ropetimer.utils.FileInfoManager
import com.lollipop.ropetimer.utils.doAsync
import com.lollipop.ropetimer.utils.onUI
import org.json.JSONObject
import java.io.File
import java.lang.ref.WeakReference

abstract class ProtocolManager : FileInfoManager() {

    protected abstract val protocolDirName: String

    protected var protocolDir: File = File("")
        private set

    protected var contextReference: WeakReference<Context>? = null
        private set

    protected var isInit = false
        private set

    override fun init(context: Context) {
        contextReference = WeakReference(context)
        protocolDir = File(File(context.filesDir, "protocol"), protocolDirName)
        if (protocolDir.isFile) {
            protocolDir.delete()
        }
        isInit = true
    }

    fun loadList(callback: (Array<String>) -> Unit) {
        if (!isInit) {
            if (BuildConfig.DEBUG) {
                throw RuntimeException("not init")
            } else {
                onUI { callback(emptyArray()) }
                return
            }
        }
        doAsync({ onUI { callback(emptyArray()) } }) {
            if (!protocolDir.exists()) {
                onUI {
                    callback(emptyArray())
                }
            } else if (protocolDir.isFile) {
                onUI {
                    callback(emptyArray())
                }
            } else {
                val list = protocolDir.list() ?: emptyArray()
                onUI {
                    callback(list)
                }
            }
        }
    }

    protected fun loadInfo(fileName: String, callback: (JSONObject) -> Unit) {
        if (!isInit) {
            if (BuildConfig.DEBUG) {
                throw RuntimeException("not init")
            } else {
                onUI { callback(JSONObject()) }
                return
            }
        }
        val name = fileName.fileNameFilter()
        if (name.isEmpty()) {
            onUI { callback(JSONObject()) }
            return
        }
        doAsync({ onUI { callback(JSONObject()) } }) {
            readObjectFromFile(
                File(protocolDir, name), {
                    onUI { callback(JSONObject()) }
                }) {
                onUI { callback(it) }
            }
        }
    }

    protected fun saveInfo(fileName: String, info: JSONObject) {
        if (!isInit) {
            if (BuildConfig.DEBUG) {
                throw RuntimeException("not init")
            } else {
                return
            }
        }
        val name = fileName.fileNameFilter()
        if (name.isEmpty()) {
            return
        }
        doAsync {
            writeToFile(info, File(protocolDir, name))
        }
    }

    protected fun String.fileNameFilter(): String {
        val input = this
        return input.replace(Regex("[^a-zA-Z0-9\\u4E00-\\u9FA5]"), "")
    }

    protected fun Cover.toJson(context: Context): JSONObject {
        return when (val cover = this) {
            is FileCover -> {
                getCoverJson(cover.file.path, FILE)
            }

            is ResourceCover -> {
                getCoverJson(cover.getName(context), RESOURCE)
            }

            else -> {
                JSONObject()
            }
        }
    }

    protected fun JSONObject.toCover(context: Context): Cover? {
        val json = this
        val typeKey = json.optString(Keys.Cover.TYPE)
        if (typeKey.isEmpty()) {
            return null
        }
        val type = CoverType.find(typeKey) ?: return null
        val value = json.optString(Keys.Cover.VALUE) ?: return null
        if (value.isEmpty()) {
            return null
        }
        return when (type) {
            FILE -> {
                FileCover(File(value))
            }

            RESOURCE -> {
                ResourceCover.fromName(context, value)
            }
        }
    }

    private fun getCoverJson(value: String, type: CoverType): JSONObject {
        return JSONObject().put(Keys.Cover.VALUE, value).put(Keys.Cover.TYPE, type.key)
    }

    private sealed class Keys {
        object Cover : Keys() {

            const val TAG = "COVER"
            const val VALUE = "VALUE"
            const val TYPE = "TYPE"

        }
    }

    private enum class CoverType(val key: String) {
        FILE("FILE"),
        RESOURCE("RESOURCE");

        companion object {
            fun find(key: String): CoverType? {
                return values().find { it.key == key }
            }
        }

    }

}