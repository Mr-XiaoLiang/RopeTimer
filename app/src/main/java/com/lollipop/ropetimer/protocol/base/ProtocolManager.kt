package com.lollipop.ropetimer.protocol.base

import android.content.Context
import com.lollipop.ropetimer.BuildConfig
import com.lollipop.ropetimer.utils.FileInfoManager
import com.lollipop.ropetimer.utils.doAsync
import com.lollipop.ropetimer.utils.onUI
import org.json.JSONObject
import java.io.File

abstract class ProtocolManager : FileInfoManager() {

    companion object {

        private var executorCount = 0

        fun getExecutorName(): String {
            executorCount++
            return "Executor$executorCount"
        }
    }

    protected abstract val protocolDirName: String

    protected var protocolDir: File = File("")
        private set

    protected var isInit = false
        private set

    override fun init(context: Context) {
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

}