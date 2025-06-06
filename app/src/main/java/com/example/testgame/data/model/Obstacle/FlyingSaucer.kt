package com.example.testgame.data.model.Obstacle

import android.graphics.Bitmap
import com.example.testgame.R

class FlyingSaucer(
    id: String = java.util.UUID.randomUUID().toString(),
    x: Float = 0f,
    y: Float = 0f,
    size: Float = 100f,
    speed: Float = 15f,
    image: Int = R.drawable.ic_fly_saucer,
    cachedBitmap: Bitmap? = null

) : Obstacle(
    id = id,
    x = x,
    y = y,
    size = size,
    speed = speed,
    image = image,
    cachedBitmap = cachedBitmap
) {

    override fun updatePosition(): Obstacle {
        val newY = y + speed * 0.2f  // bay nhanh hơn bình thường
        val newX = x + (3 * kotlin.math.sin(newY / 30)).toFloat() // zigzag nhẹ và mượt hơn
        return FlyingSaucer(
            x = newX,
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
        cachedBitmap: Bitmap?
    ): FlyingSaucer {
        return FlyingSaucer(id, x, y, size, speed, image, cachedBitmap)
    }
}





