package com.example.weathervisionapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.weathervisionapp.data.local.Constants.HOME
import com.example.weathervisionapp.data.local.Constants.SEARCH
import com.example.weathervisionapp.data.local.Constants.SETTINGS
import com.example.weathervisionapp.data.local.Constants.WISHLIST

sealed class BottomBarScreen(
    val route: String,
    val icon: ImageVector
) {

    object Home : BottomBarScreen(
        route = HOME,
        icon = Icons.Default.Home
    )

    object Search : BottomBarScreen(
        route = SEARCH,
        icon = Icons.Default.Search
    )

    object Wishlist: BottomBarScreen(
        route = WISHLIST,
        icon = Icons.Default.Favorite
    )

    object Settings : BottomBarScreen(
        route = SETTINGS,
        icon = Icons.Default.Settings
    )
}
