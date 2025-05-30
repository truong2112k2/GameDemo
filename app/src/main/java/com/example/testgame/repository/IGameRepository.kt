package com.example.testgame.repository

import android.graphics.Bitmap
import com.example.testgame.model.Obstacle
import com.example.testgame.model.Plane

interface IGameRepository {
    fun updateGameState(
        player: Plane,
        obstacles: List<Obstacle>,
        screenHeight: Float,
        screenWidth: Float,
        planeBitmap: Bitmap?, // Thêm tham số
        obstacleBitmap: Bitmap? // Thêm tham số
    ): Pair<List<Obstacle>, Boolean>
    suspend fun movePlayerLeft(plane: Plane, screenWidth: Float) : Plane
    suspend fun movePlayerRight(plane: Plane, screenWidth: Float) : Plane
}