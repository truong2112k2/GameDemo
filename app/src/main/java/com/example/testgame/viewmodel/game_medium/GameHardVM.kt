package com.example.testgame.viewmodel.game_medium

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testgame.common.GameMode
import com.example.testgame.data.model.Plane
import com.example.testgame.data.repository.IGameRepository
import com.example.testgame.mapper.toBullet
import com.example.testgame.mapper.toBulletModelUI
import com.example.testgame.mapper.toBulletModelUi
import com.example.testgame.mapper.toEnemyBullet
import com.example.testgame.mapper.toExplodeModelUI
import com.example.testgame.mapper.toObstacle
import com.example.testgame.mapper.toObstacleModel
import com.example.testgame.mapper.toPlane
import com.example.testgame.mapper.toPlaneModelUi
import com.example.testgame.view.model.BulletModelUI
import com.example.testgame.view.model.ExplodeModelUI
import com.example.testgame.view.model.ObstacleModelUI
import com.example.testgame.view.model.PlaneModelUI
import com.example.testgame.viewmodel.game_normal.ClickEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameHardVM @Inject constructor(
    private val gameIGameRepository: IGameRepository

): ViewModel() {

    var plane by mutableStateOf(PlaneModelUI())

    val obstacles = mutableStateListOf<ObstacleModelUI>()
    val bullets = mutableStateListOf<BulletModelUI>()
    val enemyBullets = mutableStateListOf<BulletModelUI>()


    private var screenWidth by mutableFloatStateOf(0f)
    private var screenHeight by mutableFloatStateOf(0f)

    private var _isGameOver = MutableStateFlow<Boolean>(false)
    var isGameOver = _isGameOver.asStateFlow()

    val explosions = mutableStateListOf<ExplodeModelUI>()

    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()


    private var planeInitialized = false


    private fun updateGameState(context: Context) {
        val result = gameIGameRepository.updateStateGame(
            plane.toPlane(),
            obstacles.map {
                it.toObstacle()
            },
            bullets.map {
                it.toBullet()
            },
            enemyBullets = enemyBullets.map {
                it.toEnemyBullet()
            },
            screenHeight,
            screenWidth,
            context,
            GameMode.HARD
        )
        if (obstacles != result.obstacles) {
            obstacles.clear()
            obstacles.addAll(result.obstacles.map {
                it.toObstacleModel()
            })
        }

        if (bullets != result.bullets) {
            bullets.clear()
            bullets.addAll(result.bullets.map { it.toBulletModelUi() })
        }
        enemyBullets.clear()
        enemyBullets.addAll(result.enemyBullets.map {
            it.toBulletModelUI()
        })
        result.explosions.forEach { addExplosion(it.toExplodeModelUI()) }

        _score.value += result.point
        _isGameOver.value = result.gameOver



        Log.d("SCORE_VM_updateGameState", "Score hiện tại: ${_score.value}, tăng thêm: ${result.point}")


    }


    private fun setScreenSize(context: Context, width: Float, height: Float) {
        screenWidth = width
        screenHeight = height

        if (!planeInitialized && width > 0 && height > 0) {

            plane = Plane(
                x = width / 2f - plane.size / 2f,
                y = height - plane.size * 2f
            ).toPlaneModelUi()
            planeInitialized = true
        }
    }


    private fun resetGame(context: Context, width: Float, height: Float) {
        obstacles.clear()
        bullets.clear()
        enemyBullets.clear()
        explosions.clear()
        _score.value = 0
        _isGameOver.value = false
        planeInitialized = false
        setScreenSize(context, width, height)
    }


    private fun movePlayerByDrag(dx: Float, dy: Float) {
        plane.toPlane().apply {
            moveBy(dx, dy, screenWidth, screenHeight)
            plane = this.toPlaneModelUi()
        }
    }


    private fun shootBullet() {
        if (_isGameOver.value) return
        if (bullets.size < 50) {
            val bullet = plane.toPlane().shoot()
            bullets.add(bullet.toBulletModelUi())
        }
    }

    private fun addExplosion(explosion: ExplodeModelUI) {
        explosions.add(explosion)
        viewModelScope.launch {
            delay(explosion.duration)
            explosions.remove(explosion)
        }
    }

    fun handleEventClick(clickEvent: ClickEvent) {
        when (clickEvent) {
            is ClickEvent.ResetGame -> {
                resetGame(clickEvent.context, clickEvent.width, clickEvent.height)
            }

            is ClickEvent.SetScreenSize -> {
                setScreenSize(clickEvent.context, clickEvent.width, clickEvent.height)
            }

            is ClickEvent.UpdateGameState -> {
                updateGameState(clickEvent.context)
            }

            is ClickEvent.MovePlayerByDrag -> {
                movePlayerByDrag(clickEvent.dx, clickEvent.dy)
            }

            ClickEvent.ShootBullet -> {
                shootBullet()
            }
        }

    }
}