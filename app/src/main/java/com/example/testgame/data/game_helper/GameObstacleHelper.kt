package com.example.testgame.data.game_helper

import android.util.Log
import com.example.testgame.common.GameMode
import com.example.testgame.data.model.Obstacle.FlyingSaucer
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Obstacle.Rocket
import com.example.testgame.data.model.Obstacle.ShootEnemy.EnemyBullet
import com.example.testgame.data.model.Obstacle.ShootEnemy.LightningPlane
import com.example.testgame.data.model.Obstacle.ShootEnemy.RaptorPlane
import com.example.testgame.data.model.Obstacle.Stone

object GameObstacleHelper {
    fun getRandomObstacleNormal(screenWidth: Float, existingObstacles: List<Obstacle>, minDistance: Float = 50f): Obstacle {
        val obstacleTypes = listOf<(Float) -> Obstacle>(
            { x -> RaptorPlane(x = x, y = 0f) },
            { x -> FlyingSaucer(x = x, y = 0f) },
            { x -> Rocket(x = x, y = 0f) },
            { x -> Stone(x = x, y = 0f) }
        )

        fun isFarEnough(newX: Float): Boolean {
            return existingObstacles.all { existing ->
                kotlin.math.abs(existing.x - newX) >= minDistance
            }
        }

        var xPos: Float
        var attempts = 0
        do {
            xPos = (0..screenWidth.toInt()).random().toFloat()
            attempts++
            if (attempts > 100) {
                break
            }
        } while (!isFarEnough(xPos))

        val obstacleFactory = obstacleTypes.random()
        return obstacleFactory(xPos)
    }


    fun getRandomObstacleMedium(screenWidth: Float, existingObstacles: List<Obstacle>, minDistance: Float = 50f): Obstacle {


        val obstacleTypes = listOf<(Float) -> Obstacle>(
            { x -> RaptorPlane(x = x, y = 0f, speed = 100f ) },
            { x -> FlyingSaucer(x = x, y = 0f, speed = 100f) },
            { x -> Rocket(x = x, y = 0f , speed = 200f) },
            { x -> Stone(x = x, y = 0f, speed = 100f) },
            { x -> LightningPlane(x = x, y = 0f, speed = 100f) },
        )

        fun isFarEnough(newX: Float): Boolean {
            return existingObstacles.all { existing ->
                kotlin.math.abs(existing.x - newX) >= minDistance
            }
        }

        var xPos: Float
        var attempts = 0
        do {
            xPos = (0..screenWidth.toInt()).random().toFloat()
            attempts++
            if (attempts > 100) {
                break
            }
        } while (!isFarEnough(xPos))

        val obstacleFactory = obstacleTypes.random()
        return obstacleFactory(xPos)

    }



    fun generateEnemyBullets(mode: GameMode, obstacles: List<Obstacle>): List<EnemyBullet> {
        return when (mode) {
            GameMode.NORMAL -> {
                obstacles.filterIsInstance<RaptorPlane>()
                    .filter { (0..100).random() < 3 }
                    .map { it.shoot().also { bullet -> Log.d("ENEMY_SHOOT", "Raptor bắn tại (${bullet.x}, ${bullet.y})") } }
            }
            GameMode.HARD -> {
                obstacles.filter { it is RaptorPlane || it is LightningPlane }
                    .filter { (0..100).random() < 6 }
                    .mapNotNull {
                        val bullet = when (it) {
                            is RaptorPlane -> it.shoot()
                            is LightningPlane -> it.shoot()
                            else -> null
                        }
                        bullet?.also {
                            Log.d("ENEMY_SHOOT_HARD", "${it::class.simpleName} bắn tại (${it.x}, ${it.y}) speed ${it.speed}")
                        }
                    }
            }
        }
    }

    fun shouldSpawnNewObstacle(mode: GameMode, random: Int): Boolean {
        return when (mode) {
            GameMode.NORMAL -> random < 3
            GameMode.HARD -> random < 5
        }
    }

    fun getNewObstacle(mode: GameMode, screenWidth: Float, currentObstacles: List<Obstacle>): Obstacle {
        return when (mode) {
            GameMode.NORMAL ->  getRandomObstacleNormal(screenWidth, currentObstacles)
            GameMode.HARD -> getRandomObstacleMedium(screenWidth, currentObstacles)
        }
    }


}