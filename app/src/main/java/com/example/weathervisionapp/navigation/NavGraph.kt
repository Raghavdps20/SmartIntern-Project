package com.example.weathervisionapp.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weathervisionapp.data.local.Constants.CITY_SCREEN
import com.example.weathervisionapp.data.local.Constants.CONFIRM
import com.example.weathervisionapp.data.local.Constants.ERROR_SCREEN
import com.example.weathervisionapp.data.local.Constants.TEXT
import com.example.weathervisionapp.data.local.Constants.TITLE
import com.example.weathervisionapp.data.viewmodel.CityViewModel
import com.example.weathervisionapp.data.viewmodel.DetailsViewModel
import com.example.weathervisionapp.data.viewmodel.HomeViewModel
import com.example.weathervisionapp.data.viewmodel.SearchViewModel
import com.example.weathervisionapp.data.viewmodel.SettingsViewModel
import com.example.weathervisionapp.ui.screen.ErrorScreen
import com.example.weathervisionapp.ui.screen.city.CityScreen
import com.example.weathervisionapp.ui.screen.home.HomeScreen
import com.example.weathervisionapp.ui.screen.search.SearchScreen
import com.example.weathervisionapp.ui.screen.settings.SettingsScreen

@ExperimentalMaterialApi
@Composable
fun NavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    searchViewModel: SearchViewModel,
    settingsViewModel: SettingsViewModel,
    cityViewModel: CityViewModel,
    detailsViewModel: DetailsViewModel,
    appTheme: MutableState<Boolean>,
) {

    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {

        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                navController = navController
            )
        }



        composable(route = BottomBarScreen.Search.route) {
            SearchScreen(
                viewModel = searchViewModel,
                navController = navController
            )
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(
                navController = navController,
                viewModel = settingsViewModel,
                appTheme = appTheme
            )
        }

        composable(route = CITY_SCREEN) {
            CityScreen(
                viewModel = cityViewModel,
                navController = navController
            )
        }

        composable(
            route = "$ERROR_SCREEN/{$TITLE}/{$TEXT}/{$CONFIRM}",
            arguments = listOf(
                navArgument(name = TITLE) {
                    type = NavType.StringType
                },
                navArgument(name = TEXT) {
                    type = NavType.StringType
                },
                navArgument(name = CONFIRM) {
                    type = NavType.StringType
                }
            )
        ) {
            ErrorScreen(
                title = it.arguments?.getString(TITLE),
                text = it.arguments?.getString(TEXT),
                confirmButton = it.arguments?.getString(CONFIRM)
            ) { navController.navigateUp() }
        }
    }
}