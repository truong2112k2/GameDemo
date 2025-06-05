package com.example.testgame

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.testgame.common.Route
import com.example.testgame.view.screen.FirstScreen
import com.example.testgame.view.screen.GameMediumScreen
import com.example.testgame.view.screen.GameNormalScreen
import com.example.testgame.viewmodel.game_medium.GameHardVM
import com.example.testgame.viewmodel.game_normal.GameEasyVM

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomNavigation(
    context: Context,
    navController: NavHostController,

    ) {

    NavHost(navController = navController, startDestination = Route.SCREEN_FIRST_ROUTE) {
        composable(Route.SCREEN_EASY_ROUTE) {
            val vm = hiltViewModel<GameEasyVM>()
            val isGameOver by vm.isGameOver.collectAsState()
            val point by vm.score.collectAsState()
            GameNormalScreen(
                context = context,
                plane = vm.plane,
                obstacles = vm.obstacles,
                bullets = vm.bullets,
                enemyBullets = vm.enemyBullets,
                isGameOver = isGameOver,
                explosions = vm.explosions,
                score = point,
                onEventClick = { vm.handleEventClick(it) }
            )
        }

        composable(Route.SCREEN_HARD_ROUTE) {
            val vm2 = hiltViewModel<GameHardVM>()
            val isGameOver by vm2.isGameOver.collectAsState()
            val point by vm2.score.collectAsState()

            GameMediumScreen(
                context = context,
                plane = vm2.plane,
                obstacles = vm2.obstacles,
                bullets = vm2.bullets,
                enemyBullets = vm2.enemyBullets,
                isGameOver = isGameOver,
                explosions = vm2.explosions,
                score = point,
                onEventClick = { vm2.handleEventClick(it) }
            )

        }
        composable(Route.SCREEN_FIRST_ROUTE) {

            FirstScreen(navController)
        }


    }
}