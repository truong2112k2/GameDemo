package com.example.testgame.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.remember
import com.example.testgame.data.GameUpdateResult
import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Explode
import com.example.testgame.data.model.Obstacle.BattleShip.EnemyBullet
import com.example.testgame.data.model.Obstacle.BattleShip.ShootEnemy
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
        enemyBullets: List<EnemyBullet>,
        screenHeight: Float,
        screenWidth: Float,
        context: Context?
    ): GameUpdateResult {


        var gameOver = false
        val explosions = mutableListOf<Explode>()

        // 1. Di chuyển obstacles
        val updatedObstacles = obstacles.map { obstacle ->
            obstacle.updatePosition()
        }

        // 2. Di chuyển player bullets
        val updatedBullets = bullets.map { it.copy(y = it.y - it.speed) }

        // 3. Di chuyển enemy bullets cũ
        val movedOldEnemyBullets = enemyBullets.map { it.copy(y = it.y + it.speed) }

        // 4. Tạo đạn mới từ ShooterEnemy
        val newlyFiredEnemyBullets = updatedObstacles
            .filterIsInstance<ShootEnemy>()
            .filter { (0..100).random() < 3 }
            .map {
                val bullet = it.shoot()
                Log.d("ENEMY_SHOOT", "Enemy bắn ra đạn tại (${bullet.x}, ${bullet.y})")
                bullet
            }

        // 5. Gộp và lọc đạn địch
        val updatedEnemyBullets = (movedOldEnemyBullets + newlyFiredEnemyBullets)
            .filter { it.y <= screenHeight }


        val planeBitmap = context?.let {
            BitmapUtils.drawableToBitmap(
                it,
                yourPlane.image,
                yourPlane.size.toInt(),
                yourPlane.size.toInt()
            )
        }

        gameOver = checkEnemyBulletHitsPlane(context, planeBitmap, updatedEnemyBullets, yourPlane, gameOver)
        gameOver = checkPlaneCollisionWithObstacles(planeBitmap, updatedObstacles, context, yourPlane, gameOver)

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
                    explosions.add(
                        Explode(
                            x = obstacle.x,
                            y = obstacle.y,
                            size = obstacle.size.toInt()
                        )
                    )
                    destroyed.add(obstacle)
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


        val newObstacles = if (random < 5) {
            val newObstacle = getRandomObstacle(screenWidth, obstacles)
            Log.d("newObstacle", newObstacle::class.simpleName.toString())

            remainingObstacles + newObstacle
        } else {
            remainingObstacles
        }

        return GameUpdateResult(
            obstacles = newObstacles,
            bullets = remainingBullets,
            gameOver = gameOver,
            explosions = explosions,
            point = destroyedCount,
            enemyBullets =  updatedEnemyBullets
        )
    }


}

private fun checkPlaneCollisionWithObstacles(
    planeBitmap: Bitmap?,
    updatedObstacles: List<Obstacle>,
    context: Context?,
    yourPlane: Plane,
    gameOver: Boolean
): Boolean {
    var gameOver1 = gameOver
    if (planeBitmap != null) {
        for (item in updatedObstacles) {
            val obstacleBitmap = context?.let {
                BitmapUtils.drawableToBitmap(
                    it,
                    item.image,
                    item.size.toInt(),
                    item.size.toInt()
                )
            }
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
                            gameOver1 = true
                            break
                        }
                    }
                    if (gameOver1) break
                }
            }
            if (gameOver1) break
        }
    }
    return gameOver1
}

private fun checkEnemyBulletHitsPlane(
    context: Context?,
    planeBitmap: Bitmap?,
    updatedEnemyBullets: List<EnemyBullet>,
    yourPlane: Plane,
    gameOver: Boolean
): Boolean {
    var gameOver1 = gameOver
    if (context != null && planeBitmap != null) {
        for (enemyBullet in updatedEnemyBullets) {

            val bulletBitmap = BitmapUtils.drawableToBitmap(
                context,
                enemyBullet.image,
                enemyBullet.size.toInt(),
                enemyBullet.size.toInt()
            )

            val collideX = enemyBullet.x < yourPlane.x + yourPlane.size &&
                    enemyBullet.x + enemyBullet.size > yourPlane.x
            val collideY = enemyBullet.y < yourPlane.y + yourPlane.size &&
                    enemyBullet.y + enemyBullet.size > yourPlane.y

            if (collideX && collideY) {
                val left = max(yourPlane.x, enemyBullet.x).toInt()
                val top = max(yourPlane.y, enemyBullet.y).toInt()
                val right = min(yourPlane.x + yourPlane.size, enemyBullet.x + enemyBullet.size).toInt()
                val bottom = min(yourPlane.y + yourPlane.size, enemyBullet.y + enemyBullet.size).toInt()

                for (x in left until right) {
                    for (y in top until bottom) {
                        val px = (x - yourPlane.x).toInt()
                        val py = (y - yourPlane.y).toInt()
                        val bx = (x - enemyBullet.x).toInt()
                        val by = (y - enemyBullet.y).toInt()

                        if (BitmapUtils.isPixelSolid(planeBitmap, px, py) &&
                            BitmapUtils.isPixelSolid(bulletBitmap, bx, by)
                        ) {
                            gameOver1 = true
                            break
                        }
                    }
                    if (gameOver1) break
                }
            }

            if (gameOver1) break
        }
    }
    return gameOver1
}

fun getRandomObstacle(screenWidth: Float, existingObstacles: List<Obstacle>, minDistance: Float = 50f): Obstacle {
    val obstacleTypes = listOf<(Float) -> Obstacle>(
        { x -> ShootEnemy(x = x, y = 0f) },
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
            // Trường hợp không tìm được vị trí phù hợp sau 100 lần thử thì thôi,
            // để tránh vòng lặp vô hạn, chấp nhận vị trí hiện tại
            break
        }
    } while (!isFarEnough(xPos))

    val obstacleFactory = obstacleTypes.random()
    return obstacleFactory(xPos)
}

//fun getRandomObstacle(screenWidth: Float): Obstacle {
//    val listObstacle = listOf(
//
//        ShootEnemy(
//            x = (0..screenWidth.toInt()).random().toFloat(),
//            y = 0f
//        ),
//
//        FlyingSaucer(
//            x = (0..screenWidth.toInt()).random().toFloat(),
//            y = 0f
//        ),
//        Rocket(
//            x = (0..screenWidth.toInt()).random().toFloat(),
//            y = 0f
//        ),
//        Stone(
//            x = (0..screenWidth.toInt()).random().toFloat(),
//            y = 0f
//        )
//    )
//    return listObstacle.random()
//}