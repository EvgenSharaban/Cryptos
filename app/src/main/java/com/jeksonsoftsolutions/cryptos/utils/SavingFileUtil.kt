package com.jeksonsoftsolutions.cryptos.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object SavingFileUtil {
    /**
     * Save an image from Uri to app-specific storage or shared storage
     * Returns a Uri that can be stored and used later
     */
    fun saveImageFromUri(context: Context, sourceUri: Uri): Uri? {
        try {
            // Open an input stream from the source Uri
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()

            return saveImageToInternalStorage(context, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * For older Android versions, save to internal app storage
     */
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

//    /**
//     * Get the stored profile image Uri as a string
//     */
//    fun getProfileImageUriString(context: Context): String? {
//        val sharedPreferences = context.getSharedPreferences("profilePrefs", Context.MODE_PRIVATE)
//        return sharedPreferences.getString("profileImageUri", null)
//    }

//    /**
//     * Store the profile image Uri as a string in preferences
//     */
//    fun saveProfileImageUriString(context: Context, uriString: String) {
//        val sharedPreferences = context.getSharedPreferences("profilePrefs", Context.MODE_PRIVATE)
//        sharedPreferences.edit {
//            putString("profileImageUri", uriString)
//        }
//    }

//    /**
//     * For Android 10+ (API 29+), save to MediaStore
//     */
//    private fun saveImageToMediaStore(context: Context, bitmap: Bitmap, fileName: String): Uri? {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/CryptoApp")
//        }
//
//        val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//        uri?.let {
//            val outputStream: OutputStream? = context.contentResolver.openOutputStream(it)
//            outputStream?.use { stream ->
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
//            }
//        }
//        return uri
//    }

//    /**
//     * For file name from Uri
//     */
//    private fun getFileNameFromUri(uri: Uri): String? {
//        var result = uri.path
//
//        val cut = result?.lastIndexOf('/')
//        if (cut != null && cut != -1) {
//            result = result.substring(cut + 1)
//        }
//        return result
//    }
}