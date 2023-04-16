package com.lollipop.ropetimer.protocol

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

interface Cover {

    fun load(imageView: ImageView)

}

class FileCover(val file: File) : Cover {
    override fun load(imageView: ImageView) {
        try {
            Glide.with(imageView).load(file).into(imageView)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

class ResourceCover(
    private val resourceId: Int
) : Cover {

    companion object {
        fun fromName(context: Context, resourceName: String): ResourceCover? {
            try {
                if (resourceName.isEmpty()) {
                    return null
                }
                val resourceId = context.resources.getIdentifier(resourceName, null, null)
                return ResourceCover(resourceId)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return null
        }

    }

    override fun load(imageView: ImageView) {
        try {
            Glide.with(imageView).load(resourceId).into(imageView)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun getName(context: Context): String {
        try {
            return context.resources.getResourceName(resourceId)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return ""
    }
}