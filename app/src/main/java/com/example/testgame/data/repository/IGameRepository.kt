package com.example.testgame.data.repository

import android.content.Context
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Plane

interface IGameRepository {

    fun updateGameState(
        yourPlane: Plane,
        obstacles: List<Obstacle>,
        screenHeight: Float,
        screenWidth: Float,
        context: Context?
    ): Pair<List<Obstacle>, Boolean>

}