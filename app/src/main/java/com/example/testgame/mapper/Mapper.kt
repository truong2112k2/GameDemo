package com.example.testgame.mapper

import com.example.testgame.R
import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Explode
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Plane
import com.example.testgame.view.model.BulletModelUI
import com.example.testgame.view.model.ExplodeModelUI
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

fun Bullet.toBulletModelUi(): BulletModelUI {
    return BulletModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image
    )
}

fun BulletModelUI.toBullet(): Bullet {
    return Bullet(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image
    )
}


fun ExplodeModelUI.toExplode(): Explode {

    return Explode(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
        duration = this.duration
    )

}

fun Explode.toExplodeModelUI(): ExplodeModelUI {
    return ExplodeModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
        duration = this.duration
    )
}

fun Obstacle.toObstacleModel(): ObstacleModelUI {
    return ObstacleModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
    )
}

fun ObstacleModelUI.toObstacle(): Obstacle {
    return Obstacle(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image
    )
}