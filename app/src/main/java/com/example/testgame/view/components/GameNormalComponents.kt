package com.example.testgame.view.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import com.example.testgame.view.model.PlaneModelUI


@Composable
fun CustomTopBar(modifier: Modifier, time: Int, point: Int, onClick: () -> Unit) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,

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

        Text(formatTime(time), color = Color.White)
        Spacer(Modifier.weight(1f))
        Text("Point: $point", color = Color.White)

    }
}

fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(minutes, secs)
}

fun DrawScope.drawPlane(x: Float, y: Float, size: Float, painter: Painter) {
    translate(left = x, top = y) {
        with(painter) {
            draw(size = Size(size, size))
        }
    }
}

fun DrawScope.drawObstacles(imageMap: List<Pair<Painter, Triple<Float, Float, Float>>>) {
    imageMap.forEach { (painter, position) ->
        translate(left = position.first, top = position.second) {
            with(painter) {
                draw(size = Size(position.third, position.third))
            }
        }
    }
}

fun DrawScope.drawBullets(bulletMap: List<Pair<Painter, Triple<Float, Float, Float>>>) {
    bulletMap.forEach { (painter, position) ->
        translate(left = position.first, top = position.second) {
            with(painter) {
                draw(size = Size(position.third, position.third))
            }
        }
    }
}

fun DrawScope.drawExplosions(explosionsMap: List<Pair<Painter, Triple<Float, Float, Int>>>) {
    explosionsMap.forEach { (painter, position) ->
        translate(left = position.first, top = position.second) {
            with(painter) {
                draw(size = Size(100f, 100f))
            }
        }
    }
}

fun DrawScope.drawEnemyBullets(bullets: List<Pair<Painter, Triple<Float, Float, Float>>>) {
    bullets.forEach { (painter, position) ->
        val (x, y, size) = position
        translate(left = x, top = y) {
            with(painter) {
                draw(size = Size(size, size))
            }
        }
    }
}



@Composable
fun GameCanvas(
    planePainter: Painter,
    plane: PlaneModelUI,
    imageMap: List<Pair<Painter, Triple<Float, Float, Float>>>,
    bulletMap: List<Pair<Painter, Triple<Float, Float, Float>>>,
    enemyBulletMap: List<Pair<Painter, Triple<Float, Float, Float>>>,

    explosionsMap: List<Pair<Painter, Triple<Float, Float, Int>>>
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawPlane(plane.x, plane.y, plane.size, planePainter)
        drawObstacles(imageMap)
        drawBullets(bulletMap)
        drawEnemyBullets(enemyBulletMap)

        drawExplosions(explosionsMap)

    }
}

@Composable
fun GameTopBar(
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



