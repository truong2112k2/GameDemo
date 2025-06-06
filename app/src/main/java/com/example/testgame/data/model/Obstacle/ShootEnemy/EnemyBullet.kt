package com.example.testgame.data.model.Obstacle.ShootEnemy

import android.graphics.Bitmap
import com.example.testgame.R


data class EnemyBullet(
    var x: Float,
    var y: Float,
    val size: Float = 25f,
    val speed: Float = 10f,
    val image: Int = R.drawable.ic_bullet_enemy,
    var cachedBitmap: Bitmap? = null

)
