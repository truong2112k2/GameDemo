package com.example.testgame.data.model

import com.example.testgame.R

data class Plane(
    var x: Float = 0f,
    var y: Float = 0f,
    val size: Float = 145f,
    val speed: Float = 30f,
    val image: Int = R.drawable.ic_plane,
) {
    fun moveBy(dx: Float, dy: Float, screenWidth: Float, screenHeight: Float) {
        x += dx
        y += dy
        x = x.coerceIn(0f, screenWidth - size)
        y = y.coerceIn(0f, screenHeight - size)
    }

    fun shoot(): Bullet {
        val bulletX = x + size / 2f - 16f
        val bulletY = y - 32f
        return Bullet(x = bulletX, y = bulletY)
    }
}
