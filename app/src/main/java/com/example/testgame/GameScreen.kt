package com.example.testgame

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testgame.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun GameScreen(modifier: Modifier = Modifier, viewModel: GameViewModel = hiltViewModel()) {


    val context = LocalContext.current


    val plane = viewModel.plane
    val obstacles = viewModel.obstacles
    val isGameOver = viewModel.gameOver
    val planePainter: Painter = painterResource(id = R.drawable.ic_plane) // Sử dụng painterResource
    val obstaclePainter: Painter = painterResource(id = R.drawable.ic_rocket2) // Sử dụng painterResource

    var height by remember { mutableFloatStateOf(0f) }
    var width by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {

        snapshotFlow { viewModel.gameOver }
            .collectLatest { gameOver ->
                if (!gameOver) {
                    while (!viewModel.gameOver) {
                        viewModel.updateGameState()
                        delay(16L)

                    }
                }
            }

    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .onGloballyPositioned {
                height =it.size.height.toFloat()
                 width =it.size.width.toFloat()
                viewModel.setScreenSize(
                    context,
                    width = it.size.width.toFloat(),
                    height = it.size.height.toFloat()
                )
            }

    ) {


        Canvas(modifier = Modifier.fillMaxSize()) {

            translate(left = plane.x, top = plane.y) {
                with(planePainter) {
                    // Bây giờ, icon sẽ được vẽ tại (0,0) của hệ tọa độ đã dịch chuyển
                    draw(size = Size(plane.size, plane.size))
                }
            }

            obstacles.forEach { obstacle ->

                 translate(left = obstacle.x, top = obstacle.y) {
                    with(obstaclePainter) {

                        draw(size = Size(obstacle.size, obstacle.size))
                    }
                }
            }
        }


        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { viewModel.movePlayerLeft() }) {
                Text("◀️")
            }

            Button(onClick = { viewModel.movePlayerRight() }) {
                Text("▶️")
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Icon(imageVector = Icons.Default.Refresh, contentDescription = "", modifier = Modifier.clickable {
                viewModel.resetGame( context, width, height)
            })


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
fun HoldableButton(
    onHold: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isHolding by remember { mutableStateOf(false) }

    LaunchedEffect(isHolding) {
        while (isHolding) {
            onHold()
            delay(50) // tốc độ di chuyển, điều chỉnh tùy bạn
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isHolding = true
                        tryAwaitRelease()
                        isHolding = false
                    }
                )
            }
    ) {
        content()
    }
}

