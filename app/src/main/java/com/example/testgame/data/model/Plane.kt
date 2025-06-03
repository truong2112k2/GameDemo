package com.example.testgame.data.model

import com.example.testgame.R

data class Plane(
    var x: Float = 0f,
    var y: Float = 0f,
    val size: Float = 145f,
    val speed: Float = 30f,
    val image: Int = R.drawable.ic_plane,
) {

}

