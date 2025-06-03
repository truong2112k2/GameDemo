package com.example.testgame.data.model.Obstacle

import com.example.testgame.R
import java.util.UUID

class FlyingSaucer(

    x: Float = 0f,
    y: Float = 0f,
    size: Float = 100f,
    speed: Float = 15f,
    image: Int = R.drawable.ic_fly_saucer,

    ) : Obstacle(
    x= x, y=y, size = size,  speed = speed, image = image
) {
}