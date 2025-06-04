package com.example.testgame.data.model.Obstacle

import com.example.testgame.R
import com.example.testgame.common.ObstacleBehavior
import java.util.UUID

class Rocket(
    id: String = UUID.randomUUID().toString(),
    x: Float = 0f,
    y: Float = 0f,
    size: Float = 60f,
    speed: Float = 18f,
    image: Int = R.drawable.ic_rocket,

    ) : Obstacle(

    x = x, y = y, size = size, speed = speed, image = image, id = id
) {
    override fun updatePosition(): Obstacle {
        val newY = y + speed
        return Rocket(
            x = x,
            y = newY,
            size = size,
            speed = speed,
            image = image,
        )
    }
    override fun copy(
        id: String,
        x: Float,
        y: Float,
        size: Float,
        speed: Float,
        image: Int,
    ): Rocket {
        return Rocket(id, x, y, size, speed, image)
    }
}