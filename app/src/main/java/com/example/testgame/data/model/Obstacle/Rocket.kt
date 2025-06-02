package com.example.testgame.data.model.Obstacle

import com.example.testgame.R

class Rocket(
     x: Float = 0f,
     y: Float = 0f,
     size: Float = 40f,
     speed: Float = 18f,
     image: Int = R.drawable.ic_rocket,

     ): Obstacle(
    x = x, y = y, size = size, speed = speed, image = image
) {

}