package com.example.testgame.data.impl

import android.content.Context
import com.example.testgame.common.GameMode
import com.example.testgame.data.GameUpdateResult
import com.example.testgame.data.game_helper.GameCollisionHelper
import com.example.testgame.data.game_helper.GameMovementHelper
import com.example.testgame.data.game_helper.GameObstacleHelper
import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Explode
import com.example.testgame.data.model.Obstacle.ShootEnemy.EnemyBullet
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Plane
import com.example.testgame.data.repository.IGameRepository
import com.example.testgame.utils.BitmapUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(

) : IGameRepository {
    override fun updateStateGame(
        yourPlane: Plane,
        obstacles: List<Obstacle>,
        bullets: List<Bullet>,
        enemyBullets: List<EnemyBullet>,
        screenHeight: Float,
        screenWidth: Float,
        context: Context?,
        mode: GameMode
    ): GameUpdateResult {

        var gameOver = false
        val explosions = mutableListOf<Explode>()

        // Di chuyển obstacle và bullet
        val updatedObstacles = GameMovementHelper.updatePositionObstacles(obstacles)
        val updatedBullets = GameMovementHelper. updatePositionBullets(bullets)
        val movedOldEnemyBullets = GameMovementHelper. updatePositionEnemyBullets(enemyBullets)

        // Bắn đạn mới từ enemy theo mode
        val newlyFiredEnemyBullets = GameObstacleHelper. generateEnemyBullets(mode, updatedObstacles)

        val updatedEnemyBullets = (movedOldEnemyBullets + newlyFiredEnemyBullets)
            .filter { it.y <= screenHeight }

        val planeBitmap = context?.let {
            BitmapUtils.drawableToBitmap(it, yourPlane.image, yourPlane.size.toInt(), yourPlane.size.toInt())
        }

        // Kiểm tra va chạm đạn enemy với máy bay
        gameOver = GameCollisionHelper.checkObstacleKillPlane(context, planeBitmap, updatedEnemyBullets, yourPlane, gameOver)

        // Kiểm tra va chạm obstacle với máy bay
        gameOver = GameCollisionHelper.checkPlaneCollisionWithObstacles(planeBitmap, updatedObstacles, context, yourPlane, gameOver)

        // Xử lý đạn trúng obstacle
        val destroyed = mutableSetOf<Obstacle>()
        val remainingObstacles = mutableListOf<Obstacle>()
        var destroyedCount = 0

        val obstacleDestroy = GameCollisionHelper.checkPlaneKillObstacle(
            updatedObstacles,
            updatedBullets,
            explosions,
            destroyed,
            destroyedCount,
            screenHeight,
            remainingObstacles
        )
        val remainingBullets = obstacleDestroy.first
        destroyedCount = obstacleDestroy.second

        // Sinh thêm obstacle theo mode
        val random = (0..100).random()
        val newObstacles = if ( GameObstacleHelper.shouldSpawnNewObstacle(mode, random)) {
            remainingObstacles + GameObstacleHelper.getNewObstacle(mode, screenWidth, obstacles)
        } else {
            remainingObstacles
        }

        return GameUpdateResult(
            obstacles = newObstacles,
            bullets = remainingBullets,
            gameOver = gameOver,
            explosions = explosions,
            point = destroyedCount,
            enemyBullets = updatedEnemyBullets
        )
    }

}


