package com.example.testgame.model

import android.graphics.Bitmap

data class Obstacle(
    var x: Float = 0f,
    var y: Float = 0f,
    val size: Float = 64f,
    val speed: Float = 8f,
    var bitmap: Bitmap? = null // Thêm thuộc tính bitmap

)
