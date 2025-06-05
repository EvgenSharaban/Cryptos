package com.jeksonsoftsolutions.cryptos.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import com.jeksonsoftsolutions.cryptos.core.other.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object SavingFileUtil {
    suspend fun saveImageFromUri(context: Context, sourceUri: Uri): Uri? = withContext(Dispatchers.IO) {
        try {
            // Open an input stream from the source Uri
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return@withContext null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            return@withContext saveImageToInternalStorage(context, bitmap)
        } catch (e: Exception) {
            Log.e(TAG, "saveImageFromUri: error", e)
            return@withContext null
        }
    }

    private fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): Uri {
        val directory = File(context.filesDir, "profile_images")
        if (!directory.exists()) {
            directory.mkdir()
        }

        val file = File(directory, "profile_image.jpg")
        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        }

        return Uri.fromFile(file)
    }
}