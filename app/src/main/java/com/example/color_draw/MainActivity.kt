package com.example.color_draw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.color_draw.ui.theme.AccessoryScreen
import com.example.color_draw.ui.theme.Color_drawTheme
import com.example.color_draw.ui.theme.EyeScreen
import com.example.color_draw.ui.theme.MouseScreen
import com.example.color_draw.ui.theme.Route

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Color_drawTheme {
                    FaceAppScreen()
            }
        }
    }
}




// 描画の記録するためにpathを表現する
@Composable
fun FaceAppScreen(
    paintViewModel: PaintViewModel  = viewModel()
){
    val navController = rememberNavController()
    val eyePaintState by paintViewModel.eyePaintState.collectAsState()
    val mousePaintState by paintViewModel.mousePaintState.collectAsState()
    val accessoryPaintState by paintViewModel.accessoryPaintState.collectAsState()

    NavHost(
        navController = navController, startDestination = Route.HOME.name
    ){
        composable(route = Route.EYE.name){
            EyeScreen(eyePaintState,navController,{ paintViewModel.setEyePaint() },{ it -> paintViewModel.updateEyePaint(it) })
        }
        composable(route = Route.MOUSE.name){
            MouseScreen(mousePaintState,navController,eyePaintState,{ paintViewModel.setMousePaint() },{ it -> paintViewModel.updateMousePaint(it) })
        }
        composable(route = Route.ACCESSORY.name) {
            AccessoryScreen(accessoryPaintState,navController,eyePaintState,mousePaintState,{ paintViewModel.setAccessoryPaint() },{ it -> paintViewModel.updateAccessoryPaint(it) })
        }
        composable(route = Route.PREVIEW.name) {
            //PaintScreen()
        }
    }
}





