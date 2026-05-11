package org.example.btvnkotlin.Week9_04_14.workers

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import org.example.btvnkotlin.R
import org.example.btvnkotlin.Week9_04_14.data.OUTPUT_PATH
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

fun getStartingImageUri(context: Context): Uri {
    return Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.mipmap.ic_launcher}")
}

fun blurBitmap(bitmap: Bitmap): Bitmap {
    val scale = 6
    val width = (bitmap.width / scale).coerceAtLeast(1)
    val height = (bitmap.height / scale).coerceAtLeast(1)
    val downScaled = Bitmap.createScaledBitmap(bitmap, width, height, true)
    return Bitmap.createScaledBitmap(downScaled, bitmap.width, bitmap.height, true)
}

fun writeBitmapToFile(
    context: Context,
    bitmap: Bitmap,
    prefix: String
): Uri {
    val outputDirectory = File(context.filesDir, OUTPUT_PATH)
    if (!outputDirectory.exists()) {
        outputDirectory.mkdirs()
    }

    val file = File(outputDirectory, "$prefix-${UUID.randomUUID()}.png")
    FileOutputStream(file).use { output ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
    }
    return Uri.fromFile(file)
}

fun decodeBitmapFromUri(context: Context, uriString: String): Bitmap? {
    val uri = Uri.parse(uriString)
    return context.contentResolver.openInputStream(uri)?.use(BitmapFactory::decodeStream)
}

