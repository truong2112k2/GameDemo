package com.example.testgame.model

import android.graphics.Bitmap

data class Plane(
    val x: Float = 0f,
    val y: Float = 0f,
    val size: Float = 100f,
    val speed: Float = 30f,
    var bitmap: Bitmap? = null // Thêm thuộc tính bitmap

)

