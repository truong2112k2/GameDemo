
package com.example.testgame.view.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testgame.view.components.GameCanvas
import com.example.testgame.view.components.GameTopBar
import com.example.testgame.view.model.BulletModelUI

import com.example.testgame.view.model.ExplodeModelUI
import com.example.testgame.view.model.ObstacleModelUI
import com.example.testgame.view.model.PlaneModelUI
import com.example.testgame.viewmodel.ClickEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    context: Context,
    plane: PlaneModelUI,
    obstacles: List<ObstacleModelUI>,
    bullets: List<BulletModelUI>,
    enemyBullets: List<BulletModelUI>,
    explosions: List<ExplodeModelUI>,
    isGameOver: Boolean,
    point: Int,
    onEventClick: (ClickEvent) -> Unit
) {
    var height by remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }
    var countDownTime by remember { mutableIntStateOf(120) }

    // Painter cache
    val painterCache = remember { mutableMapOf<Int, Painter>() }

    val planePainter = getPainter(plane.image,painterCache)

    val imageMap = obstacles.map {
        Pair(getPainter(it.image, painterCache), Triple(it.x, it.y, it.size))
    }

    val bulletMap = bullets.map {
        Pair(getPainter(it.image, painterCache), Triple(it.x, it.y, it.size))
    }

    val enemyBulletMap = enemyBullets.map {
        Pair(getPainter(it.image, painterCache), Triple(it.x, it.y, it.size))
    }

    val explosionsMap = explosions.map {
        Pair(getPainter(it.image, painterCache), Triple(it.x, it.y, it.size))
    }

    // Countdown timer
    LaunchedEffect(countDownTime, isGameOver) {
        if (countDownTime > 0 && !isGameOver) {
            delay(1000L)
            countDownTime--
        }
    }

    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            launch {
                while (true) {
                    onEventClick(ClickEvent.UpdateGameState(context))
                    delay(30L) // nhẹ hơn 16ms để tránh lag
                }
            }
            launch {
                while (true) {
                    onEventClick(ClickEvent.ShootBullet)
                    delay(300L)
                }
            }
        }
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2E2B50),
                        Color(0xFF615B9A),
                        Color(0xFF454766),
                        Color(0xFF7E53B1),
                        Color(0xFF66B0C8)
                    )
                )
            )
            .statusBarsPadding()
            .onGloballyPositioned {
                height = it.size.height.toFloat()
                width = it.size.width.toFloat()
                onEventClick(ClickEvent.SetScreenSize(context, width, height))
            }
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    val (dx, dy) = dragAmount
                    onEventClick(ClickEvent.MovePlayerByDrag(dx, dy))
                }
            }
    ) {
        GameCanvas(
            planePainter = planePainter,
            plane = plane,
            imageMap = imageMap,
            bulletMap = bulletMap,
            enemyBulletMap = enemyBulletMap,
            explosionsMap = explosionsMap
        )

        GameTopBar(
            countDownTime = countDownTime,
            point = point,
            onResetClick = {
                onEventClick(ClickEvent.ResetGame(context, width, height))
                countDownTime = 120
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .fillMaxWidth()
        )

        if (isGameOver) {
            Text(
                "Game Over",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun getPainter(imageId: Int, painterCache: MutableMap<Int, Painter>): Painter {

    return painterCache.getOrPut(imageId) { painterResource(id = imageId) }
}
