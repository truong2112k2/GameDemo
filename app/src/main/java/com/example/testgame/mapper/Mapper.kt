package com.example.testgame.mapper

import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Plane
import com.example.testgame.view.model.ObstacleModelUI
import com.example.testgame.view.model.PlaneModelUI


fun Plane.toPlaneModelUi(): PlaneModelUI {
    return PlaneModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
    )
}

fun Obstacle.toObstacleModelUi(): ObstacleModelUI {
    return ObstacleModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
    )
}

fun PlaneModelUI.toPlane(): Plane {
    return Plane(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
    )
}