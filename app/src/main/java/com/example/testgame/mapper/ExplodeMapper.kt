package com.example.testgame.mapper

import com.example.testgame.data.model.Explode
import com.example.testgame.view.model.ExplodeModelUI

fun Explode.toExplodeModelUI(): ExplodeModelUI {
    return ExplodeModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
        duration = this.duration
    )
}

