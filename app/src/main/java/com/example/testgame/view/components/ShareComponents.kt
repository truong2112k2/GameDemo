package com.example.testgame.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.testgame.R
import com.example.testgame.common.Font


@Composable
fun getPainter(imageId: Int, painterCache: MutableMap<Int, Painter>): Painter {
    return painterCache.getOrPut(imageId) { painterResource(id = imageId) }
}


@Composable
fun CustomDialogWin(
    openDialog: Boolean,
    text: String,
    score: Int,
    brush: Brush,
    onConfirm: () -> Unit,
    onReplay: () -> Unit,
    viewLeaderBoard: () -> Unit,

    ) {


    if (openDialog) {
        Dialog(onDismissRequest = { }) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        brush,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .width(300.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text, style = TextStyle(
                            brush = brush,
                            fontSize = 65.sp,
                            fontFamily = Font.CustomFontFamily

                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Your score:  ${score}", style = TextStyle(
                            brush = brush,
                            fontSize = 18.sp,
                            fontFamily = Font.CustomFontFamily

                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ButtonAlertDialog(
                            icon = {
                                Icon(
                                    painter = painterResource(R.drawable.ic_score),
                                    contentDescription = "",
                                    Modifier
                                        .size(42.dp)
                                        .padding(4.dp),
                                    tint = Color.Unspecified

                                )
                            },
                            onClick = {
                                viewLeaderBoard()
                            },
                            brush = brush
                        )
                        ButtonAlertDialog(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "",
                                    Modifier
                                        .size(42.dp)
                                        .padding(4.dp),
                                    tint = Color.Yellow

                                )
                            },
                            onClick = {
                                onReplay()
                            },
                            brush = brush
                        )

                        ButtonAlertDialog(
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "",
                                    Modifier
                                        .size(42.dp)
                                        .padding(4.dp),
                                    tint = Color.Yellow

                                )
                            },
                            onClick = {
                                onConfirm()
                            },
                            brush = brush

                        )
                    }


                }
            }
        }
    }
}

@Composable
fun ButtonAlertDialog(brush: Brush, icon: @Composable () -> Unit, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.9f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "scaleAnimation"
    )

    Box(
        modifier = Modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
            .border(
                1.dp,
                brush = brush,
                shape = RoundedCornerShape(8.dp)
            )
            .drawWithCache {
                onDrawWithContent {
                    drawContent()
                    drawRect(brush, blendMode = BlendMode.SrcAtop)
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        pressed = true
                        tryAwaitRelease()
                        pressed = false
                        onClick()
                    }
                )
            }
            .padding(14.dp)
    ) {
        icon()
    }
}
