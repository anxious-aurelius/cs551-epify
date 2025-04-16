package com.jetpack.myapplication.navigationBar

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jetpack.myapplication.home.HomeScreenContent
import com.jetpack.myapplication.home.HomeViewModel
import com.jetpack.myapplication.notification.NotificationsScreen
import com.jetpack.myapplication.SettingsScreen
import com.jetpack.myapplication.showDetails.ShowDetailScreen
import com.jetpack.myapplication.watchlist.WatchlistScreen
import com.jetpack.myapplication.watchlist.WatchlistViewModel
import com.jetpack.myapplication.watchlist.WatchlistViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.myapplication.data.WatchlistDatabase
import com.jetpack.myapplication.data.WatchlistRepository
import com.jetpack.myapplication.showList.DiscoverShowsScreen
import com.jetpack.myapplication.showList.PopularShowsScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Scaffold that holds the NavHost and the custom bottom bar.
    Scaffold(
        bottomBar = { MainScreenWithCustomBottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                val homeViewModel: HomeViewModel = viewModel()
                val uiState by homeViewModel.uiState.collectAsState()
                HomeScreenContent(
                    uiState = uiState,
                    onPopularTabSelect = { },
                    onSearchClicked = { },
                    onProfileClicked = { },
                    onShowClick = { showId ->
                        navController.navigate("details/$showId")
                    },
                    onPopularSeeAllClick = { navController.navigate("popular") },
                    onDiscoverSeeAllClick = { navController.navigate("discover") }
                )
            }
            composable("watchlist") { backStackEntry ->
                val context = LocalContext.current
                val database = WatchlistDatabase.getDatabase(context)
                val repository = WatchlistRepository(database.watchlistDao())
                val factory = WatchlistViewModelFactory(repository)
                val viewModel: WatchlistViewModel = viewModel(
                    modelClass = WatchlistViewModel::class.java,
                    factory = factory,
                    viewModelStoreOwner = backStackEntry
                )
                WatchlistScreen(
                    viewModel = viewModel,
                    onShowClick = { showId ->
                        navController.navigate("details/$showId")
                    }
                )
            }
            composable("notification") {
                NotificationsScreen()
            }
            composable("account") {
                SettingsScreen()
            }
            composable(
                "details/{showId}",
                arguments = listOf(navArgument("showId") { type = NavType.StringType })
            ) { backStackEntry ->
                val showId = backStackEntry.arguments?.getString("showId")
                if (showId.isNullOrEmpty()) {
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
}
