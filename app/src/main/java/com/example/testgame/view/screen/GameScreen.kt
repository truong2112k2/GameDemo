package com.example.testgame.view.screen

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.view.components.CustomTopBar
import com.example.testgame.view.components.drawBullets
import com.example.testgame.view.components.drawExplosions
import com.example.testgame.view.components.drawObstacles
import com.example.testgame.view.components.drawPlane
import com.example.testgame.view.model.BulletModelUI
import com.example.testgame.view.model.ExplodeModelUI
import com.example.testgame.view.model.PlaneModelUI
import com.example.testgame.viewmodel.ClickEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(
    context: Context,
    plane: PlaneModelUI,
    obstacles: List<Obstacle>,
    bullets: List<BulletModelUI>,
    explosions: List<ExplodeModelUI>,
    isGameOver: Boolean,
    point: Int,
    onEventClick: (ClickEvent) -> Unit
) {
    val planePainter =  painterResource(id = plane.image)

    var height by remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }
    var countDownTime by remember { mutableIntStateOf(120) }

    val imageMap = obstacles.map {
            Pair(painterResource(it.image), Triple(it.x, it.y, it.size))
        }



    val bulletMap = bullets.map {
            Pair(painterResource(it.image), Triple(it.x, it.y, it.size))
        }


    val explosionsMap = explosions.map {
           Pair(painterResource(it.image), Triple(it.x, it.y, it.size))
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
                    delay(16L)
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

    // Main UI
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
private fun GameCanvas(
    planePainter: Painter,
    plane: PlaneModelUI,
    imageMap: List<Pair<Painter, Triple<Float, Float, Float>>>,
    bulletMap: List<Pair<Painter, Triple<Float, Float, Float>>>,
    explosionsMap: List<Pair<Painter, Triple<Float, Float, Int>>>
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawPlane(plane.x, plane.y, plane.size, planePainter)
        drawObstacles(imageMap)
        drawBullets(bulletMap)
        drawExplosions(explosionsMap)
    }
}

@Composable
private fun GameTopBar(
    countDownTime: Int,
    point: Int,
    onResetClick: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier


    ) {
        CustomTopBar(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.3f))
                .padding(8.dp),
            time = countDownTime,
            point = point,
            onClick = onResetClick
        )
    }
}

//@Composable
//fun GameScreen(
//    context: Context,
//    plane: PlaneModelUI,
//    obstacles: List<Obstacle>,
//    bullets: List<BulletModelUI>,
//    explosions: List<ExplodeModelUI>,
//    isGameOver: Boolean,
//    point: Int,
//    onEventClick: (ClickEvent) -> Unit
//) {
//
//
//    val planePainter: Painter = painterResource(id = plane.image)
//
//    var height by remember { mutableFloatStateOf(0f) }
//    var width by remember { mutableFloatStateOf(0f) }
//
//    var countDownTime by remember { mutableIntStateOf(120) }
//
//
//    val imageMap = obstacles.map {
//        Pair(painterResource(it.image), Triple(it.x, it.y, it.size))
//    }
//
//    val bulletMap = bullets.map {
//        Pair(painterResource(it.image), Triple(it.x, it.y, it.size))
//
//    }
//    val explosionsMap = explosions.map {
//        Pair(painterResource(it.image), Triple(it.x, it.y,  it.size))
//
//    }
//
//
//
//
//    LaunchedEffect(key1 = countDownTime) { // time
//
//        if (countDownTime > 0 && !isGameOver) {
//            delay(1000L)
//
//                countDownTime--
//
//
//        } else {
//             Log.d("21321","Time game done")
//        }
//    }
//
//    LaunchedEffect(isGameOver) {
//        if (!isGameOver) {
//            while (true) {
//                onEventClick(ClickEvent.UpdateGameState(context))
//
//                 delay(16L)
//            }
//        }
//    }
//
//    LaunchedEffect(isGameOver) {
//        if (!isGameOver) {
//            while (true) {
//                onEventClick(ClickEvent.ShootBullet)
//                delay(300L)
//            }
//        }
//    }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.linearGradient(
//                    colors = listOf(
//                        Color(0xFF2E2B50),
//                        Color(0xFF615B9A),
//                        Color(0xFF454766),
//                        Color(0xFF7E53B1),
//                        Color(0xFF66B0C8)
//                    )
//                )
//            )
//            .statusBarsPadding()
//            .onGloballyPositioned {
//
//                height = it.size.height.toFloat()
//                width = it.size.width.toFloat()
//
//                onEventClick(
//                    ClickEvent.SetScreenSize(
//                        context,
//                        width = width,
//                        height = height
//                    )
//                )
//            }
//            .pointerInput(Unit) {
//                detectDragGestures { change, dragAmount ->
//                    val (dx, dy) = dragAmount
//                    onEventClick(ClickEvent.MovePlayerByDrag(dx, dy))
//                }
//            }
//
//    ) {
//
//
//
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            drawPlane(plane.x, plane.y, plane.size, planePainter)
//            drawObstacles(imageMap)
//            drawBullets(bulletMap)
//            drawExplosions(explosionsMap)
//        }
//
//        Box(
//            modifier = Modifier
//                .align(Alignment.TopEnd)
//                .padding(16.dp)
//                .fillMaxWidth(),
//        ) {
//            CustomTopBar(
//               modifier =   Modifier.fillMaxWidth().background(Color.Black.copy(alpha = 0.3f)).padding(8.dp)
//                ,
//                countDownTime,
//                point,
//                onClick = {
//
//                  onEventClick(ClickEvent.ResetGame(context, width, height))
//                }
//            )
//
//
//        }
//
//
//        if (isGameOver) {
//            Text(
//                "Game Over",
//                modifier = Modifier.align(Alignment.Center),
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//
//}
