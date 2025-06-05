package com.example.testgame.view.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.testgame.CustomNavigation
import com.example.testgame.common.CustomBrush
import com.example.testgame.common.Route
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            val navController = rememberNavController()

            val context = LocalContext.current

            CustomNavigation(
                context,
                navController
            )



//        val vm2 = hiltViewModel<GameMediumVM>()
//
//
//            val isGameOver by vm2.isGameOver.collectAsState()
//            val point by vm2.score.collectAsState()
//
//            GameMediumScreen(
//                context = context,
//                plane = vm2.plane,
//                obstacles = vm2.obstacles,
//                bullets = vm2.bullets,
//                enemyBullets = vm2.enemyBullets,
//                isGameOver = isGameOver,
//                explosions = vm2.explosions,
//                score = point,
//                onEventClick = { vm2.handleEventClick(it) }
//            )


//            val vm = hiltViewModel<GameNormalVM>()
//            val isGameOver by vm.isGameOver.collectAsState()
//            val point by vm.score.collectAsState()
//            GameNormalScreen(
//                context = context,
//                plane = vm.plane,
//                obstacles = vm.obstacles,
//                bullets = vm.bullets,
//                enemyBullets = vm.enemyBullets,
//                isGameOver = isGameOver,
//                explosions = vm.explosions,
//                score = point,
//                onEventClick = { vm.handleEventClick(it) }
//            )
        }
    }
}

@Composable
fun FirstScreen( navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize().background(
            CustomBrush.horizontalBrush
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomIconBox(
                text = "EASY",
                onClick = {
                    navController.navigate(Route.SCREEN_EASY_ROUTE)
                }
            )
            CustomIconBox(
                text = "HARD",
                onClick = {
                    navController.navigate(Route.SCREEN_HARD_ROUTE)

                }
            )
        }


    }
}

@Composable
fun CustomIconBox(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(100.dp)
            .border(1.dp, CustomBrush.verticalBrush ,RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Transparent)
            .clickable { onClick() }
            .padding(16.dp)

        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text, style = TextStyle(
                fontSize = 34.sp,
                color = Color.Black
            )
        )
    }
}
