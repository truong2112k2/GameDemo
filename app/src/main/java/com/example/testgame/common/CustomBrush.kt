package com.example.testgame.common

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object CustomBrush {

    // normal

    val verticalBrush =  Brush.verticalGradient(
        listOf(
            Color(0xFF2E2B50),
            Color(0xFF615B9A),
            Color(0xFF454766),
            Color(0xFF7E53B1),
            Color(0xFF66B0C8)
        ))

    val brightTextBrush = Brush.horizontalGradient(
        listOf(
            Color(0xFFB5AFFF), // light purple
            Color(0xFFD1C4FF), // pastel violet
            Color(0xFFF0EFFF), // very light lavender
            Color(0xFFB8D6FF), // light blue
            Color(0xFFDBFFFF)  // very light cyan
        )

    )



    val galaxyVerticalBrush = Brush.verticalGradient(
        listOf(
            Color(0xFF0B0C1C),
            Color(0xFF1B2A6A),
            Color(0xFF4A4E6D),
            Color(0xFF6B5B99),
            Color(0xFFD6D9FF)
        )
    )
}