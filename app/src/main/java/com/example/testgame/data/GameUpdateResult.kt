package com.example.testgame.data

import com.example.testgame.data.model.Bullet
import com.example.testgame.data.model.Explode
import com.example.testgame.data.model.Obstacle.Obstacle

data class GameUpdateResult(
    val obstacles: List<Obstacle>,
    val bullets: List<Bullet>,
    val gameOver: Boolean,
    val explosions: List<Explode>,
    val point: Int
)