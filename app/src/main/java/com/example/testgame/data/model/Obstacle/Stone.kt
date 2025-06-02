package com.example.testgame.data.model.Obstacle

import android.graphics.Bitmap
import com.example.testgame.R

class Stone(
     x: Float = 0f,
     y: Float = 0f,
     size: Float = 80f,
     speed: Float = 8f,
     image: Int = R.drawable.ic_stone,



): Obstacle(x, y, size, speed, image) {
}