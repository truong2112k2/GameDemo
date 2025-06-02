package com.example.testgame.view.screen

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.view.model.PlaneModelUI
import com.example.testgame.viewmodel.ClickEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GameScreen(
    context: Context,
    plane: PlaneModelUI,
    obstacles: List<Obstacle>,
    isGameOver: Boolean,
    onEventClick: (ClickEvent) -> Unit
) {



    val planePainter: Painter = painterResource(id = plane.image)

    val imageMap = obstacles.map {
        Pair(painterResource(it.image), Triple(it.x, it.y, it.size))
    }

    var height by remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }


//    LaunchedEffect(Unit) {
//          onEventClick(ClickEvent.StartGameLoop(context))
//    }

    LaunchedEffect(Unit) {

        snapshotFlow { isGameOver }
            .collectLatest { gameOver ->
                if (!gameOver) {
                    while (!isGameOver) {
                      //  viewModel.updateGameState()
                        onEventClick(ClickEvent.UpdateGameState(context))
                        delay(16L)

                    }
                }
            }

    }

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

                onEventClick(
                    ClickEvent.SetScreenSize(
                        context,
                        width = width,
                        height = height
                    )
                )
            }
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    val (dx, dy) = dragAmount
                    onEventClick(ClickEvent.MovePlayerByDrag(dx, dy))
                }
            }

    ) {


        Canvas(modifier = Modifier.fillMaxSize()) {


            translate(left = plane.x, top = plane.y) {
                with(planePainter) {
                    draw(size = Size(plane.size, plane.size))
                }
            }




            imageMap.forEach { obstacle ->
                translate(left = obstacle.second.first, top = obstacle.second.second) {
                    with(obstacle.first) {
                        draw(size = Size(obstacle.second.third, obstacle.second.third))
                    }


                }
            }
        }


        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            ResetButtonAndCountDownTime(
                Modifier.fillMaxWidth(),
                onClick = {
                    onEventClick(ClickEvent.ResetGame(context, width, height))
                }
            )


        }


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
fun ResetButtonAndCountDownTime(modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "",
            modifier = Modifier.clickable {
                onClick()
            },
            tint = Color.White
        )
        Spacer(Modifier.weight(1f))

        Text("00:00", color = Color.White)

    }
}