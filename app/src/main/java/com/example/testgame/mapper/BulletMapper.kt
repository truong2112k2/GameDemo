package com.example.testgame.mapper

import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Obstacle.ShootEnemy.EnemyBullet
import com.example.testgame.view.model.BulletModelUI


fun BulletModelUI.toEnemyBullet(): EnemyBullet{
    return EnemyBullet(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image
    )
}

fun EnemyBullet.toBulletModelUI(): BulletModelUI{
    return BulletModelUI(
        x = this.x,
        y = this.y,
        size = this.size,
        image = this.image
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
