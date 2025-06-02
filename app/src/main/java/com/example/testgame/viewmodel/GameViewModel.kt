package com.example.testgame.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testgame.data.model.Obstacle.Obstacle
import com.example.testgame.data.model.Plane
import com.example.testgame.data.repository.IGameRepository
import com.example.testgame.mapper.toPlane
import com.example.testgame.mapper.toPlaneModelUi
import com.example.testgame.view.model.PlaneModelUI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameIGameRepository: IGameRepository
) : ViewModel() {

    //var plane by mutableStateOf(Plane())
    var plane by mutableStateOf(PlaneModelUI())

    val obstacles = mutableStateListOf<Obstacle>()

    private var screenWidth by mutableFloatStateOf(0f)

    private var screenHeight by mutableFloatStateOf(0f)

    private var _isGameOver = MutableStateFlow<Boolean>(false)
    var  isGameOver = _isGameOver.asStateFlow()

    private var planeInitialized = false

//    private fun startGameLoop(context: Context) {
//        viewModelScope.launch {
//            while (!_isGameOver.value) {
//                updateGameState(context)
//                delay(16L)
//            }
//        }
//    }
    private fun updateGameState(context: Context) {

        Log.d("Check", "GameViewModel updateGameState")
        val update = gameIGameRepository.updateGameState(
            plane.toPlane(),
            obstacles,
            screenHeight,
            screenWidth,
            context
        )
        obstacles.clear()
        obstacles.addAll(update.first)
        _isGameOver.value = update.second


    }



    private fun setScreenSize(context: Context, width: Float, height: Float) {
        Log.d("Check", "GameViewModel setScreenSize")
        screenWidth = width
        screenHeight = height

        if (!planeInitialized && width > 0 && height > 0) {

            plane = Plane(
                x = width / 2f - plane.size / 2f,
                y = height - plane.size * 2f
            ).toPlaneModelUi()
            planeInitialized = true
        }
    }


    private fun resetGame(context: Context, width: Float, height: Float) {
        obstacles.clear()
        Log.d("_isGameOver", _isGameOver.value.toString())
        setScreenSize(context, width, height)
        _isGameOver.value = false
        Log.d("_isGameOver", _isGameOver.value.toString())

    }

    private  fun movePlayerByDrag(dx: Float, dy: Float) {
        plane.x += dx
        plane.y += dy

        // Giữ máy bay trong giới hạn màn hình
        if (plane.x < 0) plane.x = 0f
        if (plane.y < 0) plane.y = 0f
        if (plane.x + plane.size > screenWidth) plane.x = screenWidth - plane.size
        if (plane.y + plane.size > screenHeight) plane.y = screenHeight - plane.size
    }

    fun handleEventClick(clickEvent: ClickEvent){
        when(clickEvent){
            is ClickEvent.ResetGame -> {resetGame(clickEvent.context, clickEvent.width, clickEvent.height)}
            is ClickEvent.SetScreenSize -> { setScreenSize(clickEvent.context, clickEvent.width, clickEvent.height)}
            is ClickEvent.UpdateGameState -> {updateGameState(clickEvent.context)}
            is ClickEvent.MovePlayerByDrag -> {movePlayerByDrag(clickEvent.dx, clickEvent.dy)}
            is ClickEvent.StartGameLoop -> {
                //startGameLoop(clickEvent.context)
            }
        }

    }
}