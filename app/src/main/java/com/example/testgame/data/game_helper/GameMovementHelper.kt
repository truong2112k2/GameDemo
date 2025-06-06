package com.example.testgame.data.game_helper

import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Obstacle.ShootEnemy.EnemyBullet

object GameMovementHelper {
     fun updatePositionEnemyBullets(enemyBullets: List<EnemyBullet>): List<EnemyBullet> {
        val movedOldEnemyBullets = enemyBullets.map { it.copy(y = it.y + it.speed) }
        return movedOldEnemyBullets
    }

     fun updatePositionBullets(bullets: List<Bullet>): List<Bullet> {
        val updatedBullets = bullets.map { it.copy(y = it.y - it.speed) }
        return updatedBullets
    }

     fun updatePositionObstacles(obstacles: List<Obstacle>): List<Obstacle> {
        val updatedObstacles = obstacles.map { it.updatePosition() }
        return updatedObstacles
    }
}