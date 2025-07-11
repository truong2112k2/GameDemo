package com.example.testgame.view.screen

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.testgame.common.CustomBrush
import com.example.testgame.common.Route
import com.example.testgame.view.components.CustomDialogWin
import com.example.testgame.view.components.GameCanvas
import com.example.testgame.view.components.GameTopBar
import com.example.testgame.view.components.getPainter
import com.example.testgame.view.model.BulletModelUI
import com.example.testgame.view.model.ExplodeModelUI
import com.example.testgame.view.model.ObstacleModelUI
import com.example.testgame.view.model.PlaneModelUI
import com.example.testgame.viewmodel.game_normal.ClickEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun GameMediumScreen(
    navController: NavController,
    context: Context,
    plane: PlaneModelUI,
    obstacles: List<ObstacleModelUI>,
    bullets: List<BulletModelUI>,
    enemyBullets: List<BulletModelUI>,
    explosions: List<ExplodeModelUI>,
    isGameOver: Boolean,
    score: Int,
    onEventClick: (ClickEvent) -> Unit
) {
    var height by remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }
    var countDownTime by remember { mutableIntStateOf(120) }


    val painterCache = remember { mutableMapOf<Int, Painter>() }

    val planePainter = getPainter(plane.image, painterCache)

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
    var isGameDone by remember { mutableStateOf(false) }

    LaunchedEffect(countDownTime, isGameOver) {
        if (countDownTime > 0 && !isGameOver) {

            delay(1000L)
            countDownTime--

        } else if (!isGameOver) {

            isGameDone = true

        }
    }
    var gameJob by remember { mutableStateOf<Job?>(null) }
    var shootJob by remember { mutableStateOf<Job?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isGameOver, isGameDone) {
        if (isGameOver || isGameDone) {
            gameJob?.cancel()
            shootJob?.cancel()
            return@LaunchedEffect
        }


        gameJob = scope.launch {
            while (isActive) {
                onEventClick(ClickEvent.UpdateGameState(context))
                delay(33L)
            }
        }

        shootJob = scope.launch {
            while (isActive) {
                onEventClick(ClickEvent.ShootBullet)
                delay(400L)
            }
        }
    }


    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomBrush.galaxyVerticalBrush)
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
            point = score,
            onResetClick = {
                onEventClick(ClickEvent.ResetGame(context, width, height))
                countDownTime = 120
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .fillMaxWidth()
        )
    }

    CustomDialogWin(
        isGameDone,
        text = "Win",
        onConfirm = {
            /*
            navController.navigate(Route.SCREEN_FIRST_ROUTE) {
                popUpTo(Route.SCREEN_FIRST_ROUTE) {
                    inclusive = false // giữ lại SCREEN_FIRST_ROUTE nếu đã có
                }
                launchSingleTop = true // không tạo thêm instance nếu đã ở top
            }
             */
            navController.navigate(Route.SCREEN_FIRST_ROUTE){
                 popUpTo(
                    Route.SCREEN_HARD_ROUTE
                ){
                    inclusive = false
                }
                launchSingleTop = true
            }
        },
        onReplay = {
            onEventClick(ClickEvent.ResetGame(context, width, height))
            isGameDone = false
            countDownTime = 120
        },
        viewLeaderBoard = {

        },
        score = score,
        brush =  CustomBrush.brightTextBrush
    )


    CustomDialogWin(
        isGameOver,
        text = "Failed",
        onConfirm = {
            navController.navigate(Route.SCREEN_FIRST_ROUTE){
                popUpTo(
                    Route.SCREEN_HARD_ROUTE
                ){
                    inclusive = false
                }
                launchSingleTop = true
            }
        },
        onReplay = {
            onEventClick(ClickEvent.ResetGame(context, width, height))
            isGameDone = false
            countDownTime = 120

        },
        viewLeaderBoard = {

        },
        score = score,
        brush = CustomBrush.brightTextBrush
    )

}