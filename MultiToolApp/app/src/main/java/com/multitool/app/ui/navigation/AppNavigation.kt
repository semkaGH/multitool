package com.multitool.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.multitool.app.ui.screens.HomeScreen
import com.multitool.app.ui.screens.CalculatorScreen
import com.multitool.app.ui.screens.MinecraftAnalysisScreen
import com.multitool.app.ui.screens.YouTubeDownloaderScreen
import com.multitool.app.ui.screens.SettingsScreen
import com.multitool.app.ui.screens.AboutScreen

@Composable
fun AppNavigation() {
    val navController = NavHostController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCalculator = { navController.navigate(Screen.Calculator.route) },
                onNavigateToMinecraft = { navController.navigate(Screen.MinecraftAnalysis.route) },
                onNavigateToYouTube = { navController.navigate(Screen.YouTubeDownloader.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToAbout = { navController.navigate(Screen.About.route) }
            )
        }
        
        composable(Screen.Calculator.route) {
            CalculatorScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.MinecraftAnalysis.route) {
            MinecraftAnalysisScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.YouTubeDownloader.route) {
            YouTubeDownloaderScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        
        composable(Screen.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
