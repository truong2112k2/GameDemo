package com.example.testgame.view.model

import com.example.testgame.R

data class ObstacleModelUI(
    var x: Float = 0f,
    var y: Float = 0f,
    val size: Float = 64f,
    val image: Int = R.drawable.ic_rocket,
    val type: String = "Obstacle"

) {
}