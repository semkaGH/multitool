package com.multitool.app.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Calculator : Screen("calculator")
    object MinecraftAnalysis : Screen("minecraft_analysis")
    object YouTubeDownloader : Screen("youtube_downloader")
    object Settings : Screen("settings")
    object About : Screen("about")
}
