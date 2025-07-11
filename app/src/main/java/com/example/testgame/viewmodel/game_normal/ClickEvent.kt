package com.example.testgame.viewmodel.game_normal

import android.content.Context

sealed class ClickEvent {
    data class UpdateGameState(val context: Context) : ClickEvent()

    data class SetScreenSize(val context: Context, val width: Float, val height: Float) :
        ClickEvent()

    data class ResetGame(val context: Context, val width: Float, val height: Float) : ClickEvent()

    data class MovePlayerByDrag(val dx: Float, val dy: Float) : ClickEvent()

    data object ShootBullet: ClickEvent()

}