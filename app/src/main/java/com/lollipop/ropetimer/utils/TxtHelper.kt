package com.lollipop.ropetimer.utils

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

object TxtHelper {

    private inline fun <reified T : Closeable> T.tryClose() {
        try {
            this.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun writeToFile(value: String, file: File): Result {
        return connect(
            { ByteArrayInputStream(value.toByteArray(Charsets.UTF_8)) },
            { BufferedOutputStream(FileOutputStream(file)) }
        )
    }

    fun writeToFile(value: String, path: String) {
        writeToFile(value, File(path))
    }

    fun readFromFile(file: File): Result {
        val outputStream = ByteArrayOutputStream()
        val result = connect(
            { BufferedInputStream(FileInputStream(file)) },
            { outputStream }
        )
        return when (result) {
            is Result.Error -> {
                result
            }

            is Result.Response, Result.Successful -> {
                outputStream.flush()
                val string = outputStream.toString()
                Result.Response(string)
            }
        }
    }

    private fun connect(
        inputCreator: () -> InputStream,
        outputCreator: () -> OutputStream
    ): Result {
        var input: InputStream? = null
        var output: OutputStream? = null
        try {
            val inputStream = inputCreator()
            input = inputStream
            val outputStream = outputCreator()
            output = outputStream

            val buffer = ByteArray(2048)
            do {
                val length = inputStream.read(buffer)
                if (length >= 0) {
                    outputStream.write(buffer, 0, length)
                }
            } while (length >= 0)
            outputStream.flush()
            return Result.Successful
        } catch (e: Throwable) {
            e.printStackTrace()
            return Result.Error(e)
        } finally {
            input?.tryClose()
            output?.tryClose()
        }
    }

    sealed class Result {
        object Successful : Result()
        class Error(val throwable: Throwable) : Result()
        class Response(val value: String) : Result()
    }

    fun readFromFile(path: String): Result {
        return readFromFile(File(path))
    }

}