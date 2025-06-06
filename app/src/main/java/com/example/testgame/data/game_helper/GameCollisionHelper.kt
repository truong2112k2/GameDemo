package com.example.testgame.data.game_helper

import android.content.Context
import android.graphics.Bitmap
import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Explode
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Obstacle.ShootEnemy.EnemyBullet
import com.example.testgame.data.model.Plane
import com.example.testgame.utils.BitmapUtils
import kotlin.math.max
import kotlin.math.min

object GameCollisionHelper {
     fun checkPlaneCollisionWithObstacles(
        planeBitmap: Bitmap?,
        updatedObstacles: List<Obstacle>,
        context: Context?,
        yourPlane: Plane,
        gameOver: Boolean
    ): Boolean {
        var isGameOver = gameOver
        if (planeBitmap == null || context == null) return isGameOver

        for (item in updatedObstacles) {
            val obstacleBitmap = item.cachedBitmap ?: BitmapUtils.drawableToBitmap(
                context,
                item.image,
                item.size.toInt(),
                item.size.toInt()
            ).also { item.cachedBitmap = it }

            // Bước 1: kiểm tra va chạm bounding box
            val collideX = yourPlane.x < item.x + item.size && yourPlane.x + yourPlane.size > item.x
            val collideY = yourPlane.y < item.y + item.size && yourPlane.y + yourPlane.size > item.y

            if (collideX && collideY) {
                val left = max(yourPlane.x, item.x).toInt()
                val top = max(yourPlane.y, item.y).toInt()
                val right = min(yourPlane.x + yourPlane.size, item.x + item.size).toInt()
                val bottom = min(yourPlane.y + yourPlane.size, item.y + item.size).toInt()


                for (x in left until right step 2) {
                    for (y in top until bottom step 2) {
                        val px = (x - yourPlane.x).toInt()
                        val py = (y - yourPlane.y).toInt()
                        val ox = (x - item.x).toInt()
                        val oy = (y - item.y).toInt()

                        val planePixel = BitmapUtils.isPixelSolid(planeBitmap, px, py)
                        val obsPixel = BitmapUtils.isPixelSolid(obstacleBitmap, ox, oy)

                        if (planePixel && obsPixel) {
                            return true
                        }
                    }
                }
            }
        }

        return isGameOver
    }

     fun checkObstacleKillPlane(
        context: Context?,
        planeBitmap: Bitmap?,
        updatedEnemyBullets: List<EnemyBullet>,
        yourPlane: Plane,
        gameOver: Boolean
    ): Boolean {
        var isGameOver = gameOver
        if (context == null || planeBitmap == null) return isGameOver

        for (enemyBullet in updatedEnemyBullets) {
            // ✅ Chỉ tạo bitmap một lần, sau đó cache
            val bulletBitmap = enemyBullet.cachedBitmap ?: BitmapUtils.drawableToBitmap(
                context,
                enemyBullet.image,
                enemyBullet.size.toInt(),
                enemyBullet.size.toInt()
            ).also { enemyBullet.cachedBitmap = it }

            // Kiểm tra bounding box trước
            val collideX = enemyBullet.x < yourPlane.x + yourPlane.size &&
                    enemyBullet.x + enemyBullet.size > yourPlane.x
            val collideY = enemyBullet.y < yourPlane.y + yourPlane.size &&
                    enemyBullet.y + enemyBullet.size > yourPlane.y

            if (collideX && collideY) {
                val left = max(yourPlane.x, enemyBullet.x).toInt()
                val top = max(yourPlane.y, enemyBullet.y).toInt()
                val right = min(yourPlane.x + yourPlane.size, enemyBullet.x + enemyBullet.size).toInt()
                val bottom = min(yourPlane.y + yourPlane.size, enemyBullet.y + enemyBullet.size).toInt()

                // ✅ Duyệt cách pixel để giảm vòng lặp
                for (x in left until right step 2) {
                    for (y in top until bottom step 2) {
                        val px = (x - yourPlane.x).toInt()
                        val py = (y - yourPlane.y).toInt()
                        val bx = (x - enemyBullet.x).toInt()
                        val by = (y - enemyBullet.y).toInt()

                        if (BitmapUtils.isPixelSolid(planeBitmap, px, py) &&
                            BitmapUtils.isPixelSolid(bulletBitmap, bx, by)
                        ) {
                            return true
                        }
                    }
                }
            }
        }

        return isGameOver
    }

    fun checkPlaneKillObstacle(
        updatedObstacles: List<Obstacle>,
        updatedBullets: List<Bullet>,
        explosions: MutableList<Explode>,
        destroyed: MutableSet<Obstacle>,
        destroyedCount: Int,
        screenHeight: Float,
        remainingObstacles: MutableList<Obstacle>
    ): Pair<List<Bullet>, Int> {
        var obstacleDead = destroyedCount
        for (obstacle in updatedObstacles) {
            var isHit = false
            for (bullet in updatedBullets) {
                val collideX =
                    bullet.x < obstacle.x + obstacle.size && bullet.x + bullet.size > obstacle.x
                val collideY =
                    bullet.y < obstacle.y + obstacle.size && bullet.y + bullet.size > obstacle.y

                if (collideX && collideY) {
                    isHit = true
                    explosions.add(Explode(obstacle.x, obstacle.y, obstacle.size.toInt()))
                    destroyed.add(obstacle)
                    obstacleDead++
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
        return Pair(remainingBullets, obstacleDead)
    }


}