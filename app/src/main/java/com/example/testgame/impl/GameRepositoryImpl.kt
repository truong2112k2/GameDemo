package com.example.testgame.impl

import android.graphics.Bitmap
import android.media.Image
import com.example.testgame.Utils.BitmapUtils
import com.example.testgame.model.Obstacle
import com.example.testgame.model.Plane
import com.example.testgame.repository.IGameRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.max
import kotlin.math.min

@Singleton
class GameRepositoryImpl @Inject constructor (

): IGameRepository {
    override fun updateGameState(
        plane: Plane,
        obstacles: List<Obstacle>,
        screenHeight: Float,
        screenWidth: Float,
        planeBitmap: Bitmap?, // Thêm tham số
        obstacleBitmap: Bitmap? // Thêm tham số
    ): Pair<List<Obstacle>, Boolean> {
         var gameOver = false

        val updatedObstacles = obstacles.map {
            it.copy(y = it.y + it.speed)
        }

        if(planeBitmap != null && obstacleBitmap != null){
            for( item in updatedObstacles){
                val planeRectX = plane.x
                val planeRectY  = plane.y
                val planeRectWidth = plane.size
                val planeRectHeight = plane.size

                val obstacleRectX = item.x
                val obstacleRectY = item.y
                val obstacleRectWidth = item.size
                val obstacleRectHeight = item.size
               // Kiểm tra va chạm bounding box (AABB)
                val collideX = planeRectX < obstacleRectX + obstacleRectWidth && planeRectX + planeRectWidth > obstacleRectX
                val collideY = planeRectY < obstacleRectY + obstacleRectHeight && planeRectY + planeRectHeight > obstacleRectY

                if(collideX && collideY){
                    val interSectionLeft = max(planeRectX, obstacleRectX).toInt()
                    val interSectionTop = max(planeRectY, obstacleRectY).toInt()
                    //                    val intersectionTop = max(planeRectY, obstacleRectY).toInt()
                    val intersectionRight = min(planeRectX + planeRectWidth, obstacleRectX + obstacleRectWidth).toInt()
                    val intersectionBottom = min(planeRectY + planeRectHeight, obstacleRectY + obstacleRectHeight).toInt()

                    for( x in interSectionLeft until intersectionRight){
                        for( y in interSectionTop until  intersectionBottom){
                            val planeLocalX = (x - planeRectX).toInt()
                            val planeLocalY = (y - planeRectY).toInt()

                            val obstacleLocalX = (x - obstacleRectX).toInt()
                            val obstacleLocalY = (y - obstacleRectY).toInt()

                            val planePixelSolid = BitmapUtils.isPixelSolid(planeBitmap, planeLocalX, planeLocalY)
                            //  val obstaclePixelSolid = BitmapUtils.isPixelSolid(obstacleBitmap, obstacleLocalX, obstacleLocalY)
                            val obstaclePixelSolid = BitmapUtils.isPixelSolid(obstacleBitmap, obstacleLocalX, obstacleLocalY)

                            if(planePixelSolid && obstaclePixelSolid){
                                gameOver = true
                                break
                            }

                        }
                        if( gameOver) break
                    }
                }
                if(gameOver) break
            }
        }

        ////-------------------------------------------------------------
        val visibleObstacles = updatedObstacles.filter {
            it.y <= screenHeight
        }

        val newObstacles = if((0..100).random() < 5){
            visibleObstacles + Obstacle(
                x = (0..screenWidth.toInt()).random().toFloat(),
                y = 0f,
                bitmap = obstacleBitmap // SỬA LỖI NÀY: Gán bitmap cho vật cản mới

            )
        }else{
            visibleObstacles

        }

        return Pair(newObstacles, gameOver)

    }

    override suspend fun movePlayerLeft(plane: Plane, screenWidth: Float): Plane {
        return plane.copy(x = max(0f, plane.x - plane.speed)) // Giảm x
    }

    override suspend fun movePlayerRight(plane: Plane, screenWidth: Float): Plane {
        return plane.copy(x = min(screenWidth - plane.size, plane.x + plane.speed)) // Tăng x
    }


}