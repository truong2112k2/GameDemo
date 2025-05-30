package com.example.testgame.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat

object BitmapUtils {

    // Chuyển đổi Drawable ID (như R.drawable.ic_plane) thành Bitmap
    fun drawableToBitmap(context: Context, @androidx.annotation.DrawableRes drawableId: Int, targetWidth: Int, targetHeight: Int): Bitmap? {
        val drawable: Drawable? = ContextCompat.getDrawable(context, drawableId)

        return when (drawable) {
            is BitmapDrawable -> {
                Bitmap.createScaledBitmap(drawable.bitmap, targetWidth, targetHeight, true)
            }
            is VectorDrawable -> {
                val bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bitmap
            }
            else -> null
        }
    }

    // Kiểm tra pixel tại tọa độ (x, y) có trong suốt hay không
    // (x, y) là tọa độ tương đối trong phạm vi của bitmap
    fun isPixelSolid(bitmap: Bitmap?, x: Int, y: Int): Boolean {
        if (bitmap == null || x < 0 || y < 0 || x >= bitmap.width || y >= bitmap.height) {
            return false // Ngoài phạm vi hoặc bitmap null
        }
        val pixel = bitmap.getPixel(x, y)
        val alpha = android.graphics.Color.alpha(pixel)
        return alpha > 0 // Pixel không trong suốt hoàn toàn
    }
}