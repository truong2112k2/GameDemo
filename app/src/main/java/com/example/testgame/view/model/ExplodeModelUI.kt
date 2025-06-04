package com.example.testgame.view.model

import com.example.testgame.R

data class ExplodeModelUI(
    val x: Float,
    val y: Float,
    val size: Int,
    val image: Int = R.drawable.ic_explode,
    var duration: Long = 200L
) {
}