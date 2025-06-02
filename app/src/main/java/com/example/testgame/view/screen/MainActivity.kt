package com.example.testgame.view.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.testgame.view.theme.TestGameTheme
import com.example.testgame.viewmodel.GameViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val context = LocalContext.current
            val vm = hiltViewModel<GameViewModel>()

            val isGameOver by vm.isGameOver.collectAsState()
            GameScreen(
                context = context,
                plane = vm.plane,
                obstacles = vm.obstacles,
                isGameOver = isGameOver,
                onEventClick = { vm.handleEventClick(it) }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestGameTheme {
    }
}