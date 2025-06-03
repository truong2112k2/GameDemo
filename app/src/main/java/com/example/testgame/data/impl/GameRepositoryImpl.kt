package com.example.testgame.data.impl

import android.content.Context
import android.util.Log
import com.example.testgame.data.GameUpdateResult
import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Explode
import com.example.testgame.data.model.Obstacle.FlyingSaucer
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Obstacle.Rocket
import com.example.testgame.data.model.Obstacle.Stone
import com.example.testgame.data.model.Plane
import com.example.testgame.data.repository.IGameRepository
import com.example.testgame.utils.BitmapUtils
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

@Singleton
class GameRepositoryImpl @Inject constructor(

) : IGameRepository {

    override fun updateGameState(
        yourPlane: Plane,
        obstacles: List<Obstacle>,
        bullets: List<Bullet>,
        screenHeight: Float,
        screenWidth: Float,
        context: Context?
    ): GameUpdateResult {

        var gameOver = false
        val explosions = mutableListOf<Explode>()

        val updatedObstacles = obstacles.map { it.copy(y = it.y + it.speed) }
        val updatedBullets = bullets.map { it.copy(y = it.y - it.speed) }

        val planeBitmap = context?.let {
            BitmapUtils.drawableToBitmap(
                it,
                yourPlane.image,
                yourPlane.size.toInt(),
                yourPlane.size.toInt()
            )
        }

        if (planeBitmap != null) {
            for (item in updatedObstacles) {
                val obstacleBitmap = BitmapUtils.drawableToBitmap(
                    context,
                    item.image,
                    item.size.toInt(),
                    item.size.toInt()
                )
                val collideX =
                    yourPlane.x < item.x + item.size && yourPlane.x + yourPlane.size > item.x
                val collideY =
                    yourPlane.y < item.y + item.size && yourPlane.y + yourPlane.size > item.y

                if (collideX && collideY) {
                    val left = max(yourPlane.x, item.x).toInt()
                    val top = max(yourPlane.y, item.y).toInt()
                    val right = min(yourPlane.x + yourPlane.size, item.x + item.size).toInt()
                    val bottom = min(yourPlane.y + yourPlane.size, item.y + item.size).toInt()

                    for (x in left until right) {
                        for (y in top until bottom) {
                            val px = (x - yourPlane.x).toInt()
                            val py = (y - yourPlane.y).toInt()
                            val ox = (x - item.x).toInt()
                            val oy = (y - item.y).toInt()

                            val planePixel = BitmapUtils.isPixelSolid(planeBitmap, px, py)
                            val obsPixel = BitmapUtils.isPixelSolid(obstacleBitmap, ox, oy)

                            if (planePixel && obsPixel) {
                                gameOver = true
                                break
                            }
                        }
                        if (gameOver) break
                    }
                }
                if (gameOver) break
            }
        }

        val destroyed = mutableSetOf<Obstacle>()
        val remainingObstacles = mutableListOf<Obstacle>()

        var destroyedCount = 0

        for (obstacle in updatedObstacles) {
            var isHit = false
            for (bullet in updatedBullets) {
                val collideX = bullet.x < obstacle.x + obstacle.size && bullet.x + bullet.size > obstacle.x
                val collideY = bullet.y < obstacle.y + obstacle.size && bullet.y + bullet.size > obstacle.y

                if (collideX && collideY) {
                    isHit = true
                    explosions.add(Explode(x = obstacle.x, y = obstacle.y, size = obstacle.size.toInt()))
                    destroyedCount++
                    break
                }
            }

            if (!isHit && obstacle.y <= screenHeight) {
                remainingObstacles.add(obstacle)
            }
        }


        val remainingBullets = updatedBullets.filter { bullet ->
            bullet.y > 0 && !destroyed.any {
                bullet.x < it.x + it.size && bullet.x + bullet.size > it.x &&
                        bullet.y < it.y + it.size && bullet.y + bullet.size > it.y
            }
        }

        val random = (0..100).random()

        val listObstacle = listOf(
            Obstacle(
                x = (0..screenWidth.toInt()).random().toFloat(),
                y = 0f
            ),
            FlyingSaucer(
                x = (0..screenWidth.toInt()).random().toFloat(),
                y = 0f
            ),
            Rocket(
                x = (0..screenWidth.toInt()).random().toFloat(),
                y = 0f
            ),
            Stone(
                x = (0..screenWidth.toInt()).random().toFloat(),
                y = 0f
            )
        )
        val newObstacles = if (random < 5) {
            val newObstacle = listObstacle.random()
            remainingObstacles + newObstacle
        } else {
            remainingObstacles
        }
        Log.d("312321", destroyedCount.toString())

        return GameUpdateResult(
            obstacles = newObstacles,
            bullets = remainingBullets,
            gameOver = gameOver,
            explosions = explosions,
            point = destroyedCount
        )
    }


}