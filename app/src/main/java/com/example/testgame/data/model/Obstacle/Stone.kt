package com.example.testgame.data.model.Obstacle

import com.example.testgame.R
import java.util.UUID

class Stone(
    id: String = UUID.randomUUID().toString(),

    x: Float = 0f,
    y: Float = 0f,
    size: Float = 80f,
    speed: Float = 8f,
    image: Int = R.drawable.ic_stone,


) : Obstacle(
    id = id,
    x = x,
    y = y,
    size = size,
    speed = speed,
    image = image,
) {

    override fun updatePosition(): Obstacle {
        /*
         val newY = y + speed
        return Rocket(
            x = x,
            y = newY,
            size = size,
            speed = speed,
            image = image,
        )
         */
        val newY = y + speed
        return Stone(
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
    ): Stone {
        return Stone(id, x, y, size, speed, image)
    }
}