package com.example.testgame.mapper

import com.example.testgame.data.model.Obstacle.BattleShip.ShootEnemy
import com.example.testgame.data.model.Obstacle.FlyingSaucer
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Obstacle.Rocket
import com.example.testgame.data.model.Obstacle.Stone
import com.example.testgame.view.model.ObstacleModelUI

fun Obstacle.toObstacleModel(): ObstacleModelUI {
    return ObstacleModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image,
        type = this::class.simpleName ?: "Obstacle"
    )
}

fun ObstacleModelUI.toObstacle(): Obstacle {
    return when (this.type) {
        "Stone" -> Stone(
            x = this.x,
            y = this.y,
            size = this.size,
            image = this.image
        )

        "Rocket" -> Rocket(
            x = this.x,
            y = this.y,
            size = this.size,
            image = this.image
        )

        "FlyingSaucer" -> FlyingSaucer(
            x = this.x,
            y = this.y,
            size = this.size,
            image = this.image
        )

        "ShootEnemy" -> ShootEnemy(
            x = this.x,
            y = this.y,
            size = this.size,
            image = this.image
        )

        else -> Obstacle(
            x = this.x,
            y = this.y,
            size = this.size,
            image = this.image
        )
    }
}