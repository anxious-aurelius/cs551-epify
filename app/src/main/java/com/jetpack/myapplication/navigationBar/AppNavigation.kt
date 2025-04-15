package com.jetpack.myapplication.navigationBar

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.jetpack.myapplication.home.HomeScreenContent
import com.jetpack.myapplication.home.HomeViewModel
import com.jetpack.myapplication.showDetails.ShowDetailScreen
import com.jetpack.myapplication.notification.NotificationsScreen
import com.jetpack.myapplication.SettingsScreen
import com.jetpack.myapplication.showDetails.ShowDetailViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.myapplication.showList.DiscoverShowsScreen
import com.jetpack.myapplication.showList.PopularShowsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "main") {

        composable("main") {
            MainScreenWithCustomBottomBar(navController)
        }

        composable(
            "details/{showId}",
            arguments = listOf(navArgument("showId") { type = NavType.StringType })
        ) { backStackEntry ->
            val showId = backStackEntry.arguments?.getString("showId")

            if (showId.isNullOrEmpty()) {
                // Log error or navigate back
                Log.e("AppNavigation", "Invalid showId passed to details route")
                navController.popBackStack()
                return@composable
            }
            ShowDetailScreen(
                showId = showId,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("popular") {
            PopularShowsScreen(onShowClick = { showId -> navController.navigate("details/$showId") })
        }

        composable("discover") {
            DiscoverShowsScreen(onShowClick = { showId -> navController.navigate("details/$showId") })
        }

    }

}
