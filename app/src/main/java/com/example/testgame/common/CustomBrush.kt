package com.example.testgame.common

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object CustomBrush {

    val horizontalBrush =  Brush.horizontalGradient(
        listOf(
            Color(0xFF2E2B50),
            Color(0xFF615B9A),
            Color(0xFF454766),
            Color(0xFF7E53B1),
            Color(0xFF66B0C8)
        ))


    val verticalBrush =  Brush.verticalGradient(
        listOf(
            Color(0xFF2E2B50),
            Color(0xFF615B9A),
            Color(0xFF454766),
            Color(0xFF7E53B1),
            Color(0xFF66B0C8)
        ))
}