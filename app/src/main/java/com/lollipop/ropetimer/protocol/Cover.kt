package com.lollipop.ropetimer.protocol

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.io.File

interface Cover {

    fun load(imageView: ImageView)

}

class FileCover(private val file: File) : Cover {
    override fun load(imageView: ImageView) {
        try {
            Glide.with(imageView).load(file).into(imageView)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

class ResourceCover(private val resourceId: Int) : Cover {
    override fun load(imageView: ImageView) {
        try {
            Glide.with(imageView).load(resourceId).into(imageView)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}