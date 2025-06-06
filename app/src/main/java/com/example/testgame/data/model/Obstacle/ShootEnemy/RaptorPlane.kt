package com.example.testgame.data.model.Obstacle.ShootEnemy


import android.graphics.Bitmap
import com.example.testgame.R
import com.example.testgame.data.model.Obstacle.Obstacle
import java.util.UUID

class RaptorPlane(
     id: String = UUID.randomUUID().toString(),
     x: Float = 0f,
     y: Float = 0f,
     size: Float = 80f,
     speed: Float = listOf(10f, 20f, 30f, 40f, 50f).random(),
     image: Int = R.drawable.ic_spaceship,
     cachedBitmap: Bitmap? = null

) : Obstacle( id,  x, y, size, speed, image,cachedBitmap) {



    fun shoot(
    ): EnemyBullet {
        val bulletX = x + size / 2f - 16f
        val bulletY = y + size
        return EnemyBullet(x = bulletX, y = bulletY)
    }

    override fun copy(
        id: String,
        x: Float,
        y: Float,
        size: Float,
        speed: Float,
        image: Int,
        cachedBitmap: Bitmap?
    ): Obstacle {
        return RaptorPlane(
            id,
            x,
            y,
            size,
            speed,
            image,
            cachedBitmap
        )
    }


    override fun updatePosition(): Obstacle {


        val newX = x + (3 * kotlin.math.sin(y / 30)).toFloat()
        val newY = y + (1.5 * kotlin.math.sin(x / 50)).toFloat()
        return RaptorPlane(
            x = newX,
            y = newY,
            size = size,
            speed = speed,
            image = image
        )
    }

}
