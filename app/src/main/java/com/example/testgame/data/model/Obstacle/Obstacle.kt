package com.example.testgame.data.model.Obstacle

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.testgame.R


open class Obstacle(
    var x: Float = 0f,
    var y: Float = 0f,
    val size: Float = 64f,
    val speed: Float = 8f,
    val image: Int = R.drawable.ic_rocket,

    ) {
    open fun copy(
        x: Float = this.x,
        y: Float = this.y,
        size: Float = this.size,
        speed: Float = this.speed,
        image: Int = this.image
    ): Obstacle {
        return Obstacle(x, y, size, speed, image)
    }

}