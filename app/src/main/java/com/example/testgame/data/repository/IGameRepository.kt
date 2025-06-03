package com.example.testgame.data.repository

import android.content.Context
import com.example.testgame.data.GameUpdateResult
import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Plane

interface IGameRepository {


fun updateGameState(
    yourPlane: Plane,
    obstacles: List<Obstacle>,
    bullets: List<Bullet>,
    screenHeight: Float,
    screenWidth: Float,
    context: Context?
):  GameUpdateResult


}