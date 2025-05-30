package com.example.testgame.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testgame.R
import com.example.testgame.Utils.BitmapUtils
import com.example.testgame.model.Obstacle
import com.example.testgame.model.Plane
import com.example.testgame.repository.IGameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameIGameRepository: IGameRepository
) : ViewModel() {

   // var plane by mutableStateOf(Plane(3000f, 1000f))
    var plane by mutableStateOf(Plane())
    val obstacles = mutableStateListOf<Obstacle>()
    var screenWidth by mutableFloatStateOf(0f)
    var screenHeight by mutableFloatStateOf(0f)
    var gameOver by mutableStateOf(false)

    private var planeBitmap: Bitmap? = null
    private var obstacleBitmap: Bitmap? = null

     fun updateGameState() {

        val update = gameIGameRepository.updateGameState(
            plane,
            obstacles,
            screenHeight,
            screenWidth,
            planeBitmap,
            obstacleBitmap
        )
        obstacles.clear()
        obstacles.addAll(update.first)
        gameOver = update.second


    }
    fun movePlayerLeft() {
        viewModelScope.launch {
            plane = gameIGameRepository.movePlayerLeft(plane, screenWidth)
        }
    }

    fun movePlayerRight() {
        viewModelScope.launch {
            plane = gameIGameRepository.movePlayerRight(plane, screenWidth)
        }
    }
    private var planeInitialized = false // thêm biến này vào ViewModel

    fun setScreenSize(context: Context, width: Float, height: Float) {
        screenWidth = width
        screenHeight = height

        if (!planeInitialized && width > 0 && height > 0) {
            // Tải bitmap khi kích thước màn hình được biết
            // Quan trọng: Kích thước bitmap phải khớp với kích thước 'size' của đối tượng để pixel mapping chính xác
            planeBitmap = BitmapUtils.drawableToBitmap(context, R.drawable.ic_plane, plane.size.toInt(), plane.size.toInt())
            obstacleBitmap = BitmapUtils.drawableToBitmap(context, R.drawable.ic_rocket, Obstacle().size.toInt(), Obstacle().size.toInt())

            plane = Plane(
                x = width / 2f - plane.size / 2f,
                y = height - plane.size * 2f, // Đặt máy bay ở gần cuối màn hình
                bitmap = planeBitmap // Gán bitmap vào đối tượng plane
            )
            planeInitialized = true
        }
    }

    fun resetGame( context: Context,width: Float, height: Float) {
        obstacles.clear()
        gameOver = false
        setScreenSize(context, width, height)
    }
}