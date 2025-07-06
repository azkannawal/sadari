package com.example.sadari

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sadari.ui.theme.SadariTheme
import com.example.sadari.view.DetectorScreen
import com.example.sadari.view.DrawSenseScreen
import com.example.sadari.view.HomeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            SadariTheme {
              MainApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Daftar halaman yang boleh menampilkan BottomBar
    val bottomBarRoutes = listOf("meditation", "ai", "home", "consultation", "profile")

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                CustomBottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        AnimatedNavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues),
            enterTransition = { fadeIn(animationSpec = tween(100)) },
            exitTransition = { fadeOut(animationSpec = tween(100)) },
            popEnterTransition = { fadeIn(animationSpec = tween(100)) },
            popExitTransition = { fadeOut(animationSpec = tween(100)) }
        ) {
            composable("home") {
                HomeScreen(navController)
            }
            composable("drawsense") {
                DrawSenseScreen()
            }
            composable("meditation") {
                DetectorScreen()
            }
            composable("ai") {
                DetectorScreen()
            }
            composable("profile") {
                DetectorScreen()
            }
            composable("consultation") {
                DetectorScreen()
            }
        }
    }
}


@Composable
fun CustomBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
        val items = listOf("meditation", "ai", "home", "consultation", "profile")
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        ) {
            items.forEach { screen ->
                val isSelected = currentRoute == screen
                val icon = when (screen) {
                    "meditation" -> R.drawable.frame_13
                    "ai" -> R.drawable.frame_12
                    "home" -> R.drawable.frame_13a
                    "consultation" -> R.drawable.frame_14
                    "profile" -> R.drawable.frame_15
                    else -> R.drawable.frame_15
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            brush = if (isSelected) {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFD0CBFF),
                                        Color(0xFFB3D6EA),
                                    )
                                )
                            } else {
                                Brush.verticalGradient( // gradient dari abu muda ke abu muda (seolah solid)
                                    colors = listOf(
                                        Color(0xFFF0F0F0),
                                        Color(0xFFF0F0F0)
                                    )
                                )
                            }
                        )
                        .clickable {
                            if (!isSelected) {
                                navController.navigate(screen) {
                                    popUpTo("home") { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = screen,
                        modifier = Modifier
                            .size(24.dp),
                        colorFilter = ColorFilter.tint(
                        if (isSelected) Color.White else Color.Gray
                        )
                    )
                }
            }
        }
    }
}