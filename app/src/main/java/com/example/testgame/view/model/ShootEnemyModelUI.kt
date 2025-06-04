package com.example.testgame.view.model

import com.example.testgame.R
import java.util.UUID

data class ShootEnemyModelUI(
    val id: String = UUID.randomUUID().toString(),
    val x: Float = 0f,
    val y: Float = 0f,
    val size: Float = 80f,
    val speed: Float = 8f,
    val image: Int = R.drawable.ic_spaceship,
) {
}