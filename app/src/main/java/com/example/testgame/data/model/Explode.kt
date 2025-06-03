package com.example.testgame.data.model

import com.example.testgame.R

data class Explode(
    val x: Float,
    val y: Float,
    val size: Int,
    val image: Int = R.drawable.ic_explode,
    var duration: Long = 200L
)