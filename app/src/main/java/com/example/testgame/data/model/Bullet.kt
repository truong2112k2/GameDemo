package com.example.testgame.data.model

import com.example.testgame.R


data class Bullet(
    var x: Float,
    var y: Float,
    val size: Float = 14f,
    val speed: Float = 8f,
    val image: Int = R.drawable.ic_bullet_plane
)