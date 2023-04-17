package com.lollipop.ropetimer.protocol.normal

import com.lollipop.ropetimer.protocol.FileCover
import com.lollipop.ropetimer.protocol.NormalProtocol
import com.lollipop.ropetimer.protocol.base.ProtocolManager
import java.io.File

class NormalProtocolManager : ProtocolManager() {
    override val protocolDirName: String = "normal"

    private fun removeProtocolFile(file: File, protocol: NormalProtocol) {
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