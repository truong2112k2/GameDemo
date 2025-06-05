package com.example.testgame.mapper

import com.example.testgame.data.model.Obstacle.ShootEnemy.RaptorPlane
import com.example.testgame.view.model.ShootEnemyModelUI

fun RaptorPlane.toShootEnemyModelUI(): ShootEnemyModelUI {
    return ShootEnemyModelUI(
        id = this.id,
        x = this.x,
        y = this.y,
        size = this.size,
        speed = this.speed,
        image = this.image,
    )
}
