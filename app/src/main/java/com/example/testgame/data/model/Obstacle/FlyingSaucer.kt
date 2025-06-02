package com.example.testgame.data.model.Obstacle

import android.graphics.Bitmap
import com.example.testgame.R

class FlyingSaucer(
    x: Float = 0f,
    y: Float = 0f,
    size: Float = 55f,
    speed: Float = 15f,
    image: Int = R.drawable.ic_fly_saucer,

): Obstacle(
    x, y, size, speed, image
){
}