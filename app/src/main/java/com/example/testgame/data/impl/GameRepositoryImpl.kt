package com.example.testgame.data.impl

import android.content.Context
import android.util.Log
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
        screenHeight: Float,
        screenWidth: Float,
        context: Context?
    ): Pair<List<Obstacle>, Boolean> {

        var gameOver = false

        val updatedObstacles = obstacles.map {
            it.copy(y = it.y + it.speed)
        }

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

                val planeRectX = yourPlane.x
                val planeRectY = yourPlane.y
                val planeRectWidth = yourPlane.size
                val planeRectHeight = yourPlane.size

                val obstacleRectX = item.x
                val obstacleRectY = item.y
                val obstacleRectWidth = item.size
                val obstacleRectHeight = item.size
                // Kiểm tra va chạm bounding box (AABB)
                val collideX =
                    planeRectX < obstacleRectX + obstacleRectWidth && planeRectX + planeRectWidth > obstacleRectX
                val collideY =
                    planeRectY < obstacleRectY + obstacleRectHeight && planeRectY + planeRectHeight > obstacleRectY

                if (collideX && collideY) {
                    val interSectionLeft = max(planeRectX, obstacleRectX).toInt()
                    val interSectionTop = max(planeRectY, obstacleRectY).toInt()
                    val intersectionRight =
                        min(planeRectX + planeRectWidth, obstacleRectX + obstacleRectWidth).toInt()
                    val intersectionBottom = min(
                        planeRectY + planeRectHeight,
                        obstacleRectY + obstacleRectHeight
                    ).toInt()

                    for (x in interSectionLeft until intersectionRight) {
                        for (y in interSectionTop until intersectionBottom) {
                            val planeLocalX = (x - planeRectX).toInt()
                            val planeLocalY = (y - planeRectY).toInt()

                            val obstacleLocalX = (x - obstacleRectX).toInt()
                            val obstacleLocalY = (y - obstacleRectY).toInt()

                            val planePixelSolid =
                                BitmapUtils.isPixelSolid(planeBitmap, planeLocalX, planeLocalY)

                            val obstaclePixelSolid = BitmapUtils.isPixelSolid(
                                obstacleBitmap,
                                obstacleLocalX,
                                obstacleLocalY
                            )

                            if (planePixelSolid && obstaclePixelSolid) {
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

        val visibleObstacles = updatedObstacles.filter {
            it.y <= screenHeight
        }
        val randomObstacle = (0..100).random()
        val newObstacles = if (randomObstacle < 5) {
            val newObstacle = when (randomObstacle) {
                0 -> {
                    Rocket(
                        x = (0..screenWidth.toInt()).random().toFloat(),
                        y = 0f,
                    )
                }

                1 -> {
                    FlyingSaucer(
                        x = (0..screenWidth.toInt()).random().toFloat(),
                        y = 0f,
                    )
                }

                2 -> {
                    Stone(
                        x = (0..screenWidth.toInt()).random().toFloat(),
                        y = 0f,
                    )
                }

                3 -> {
                    Stone(
                        x = (0..screenWidth.toInt()).random().toFloat(),
                        y = 0f,
                    )
                }

                4 -> {
                    Rocket(
                        x = (0..screenWidth.toInt()).random().toFloat(),
                        y = 0f,
                    )
                }

                else -> {
                    Rocket(
                        x = (0..screenWidth.toInt()).random().toFloat(),
                        y = 0f,
                    )
                }
            }
            Log.d("312321", newObstacle.toString())

            visibleObstacles + newObstacle

        } else {
            visibleObstacles

        }

        return Pair(newObstacles, gameOver)

    }


}