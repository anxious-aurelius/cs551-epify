package com.jetpack.myapplication.navigationBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jetpack.myapplication.SettingsScreen
import com.jetpack.myapplication.home.HomeScreen
import com.jetpack.myapplication.home.HomeScreenContent
import com.jetpack.myapplication.home.HomeViewModel
import com.jetpack.myapplication.notification.NotificationsScreen
import com.jetpack.myapplication.showDetails.ShowDetailScreen
import com.jetpack.myapplication.showList.DiscoverShowsScreen
import java.net.URLEncoder

@Composable
fun MainScreenWithCustomBottomBar(navController: NavController) { // Accept NavController!

    val bottomBarItems = listOf(
        BottomBarItem(route = "home", label = "Home", icon = Icons.Default.Home),
        BottomBarItem(route = "watchlist", label = "Watchlist", icon = Icons.Default.CheckCircle),
        BottomBarItem(route = "notification", label = "Notification", icon = Icons.Default.Notifications),
        BottomBarItem(route = "account", label = "Account", icon = Icons.Default.Person)
    )

    var selectedRoute by remember { mutableStateOf("home") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            CurvedBottomBar(
                items = bottomBarItems,
                selectedItem = selectedRoute,
                onItemSelected = { route ->
                    selectedRoute = route
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedRoute) {
                "notification" -> NotificationsScreen()

                "watchlist" -> DiscoverShowsScreen(
                    onShowClick = { showId ->
                        navController.navigate("details/$showId")
                    }
                )

                "account" -> SettingsScreen()

                "home" -> {
                    val homeViewModel: HomeViewModel = viewModel()
                    val uiState by homeViewModel.uiState.collectAsState()

                    HomeScreenContent(
                        uiState = uiState,
                        onPopularTabSelect = {  },
                        onSearchClicked = {  },
                        onProfileClicked = {  },
                        onShowClick = { showId -> val encodedId = URLEncoder.encode(showId, "UTF-8")
                                                    navController.navigate("details/$encodedId") },
                        onPopularSeeAllClick = { navController.navigate("popular") },
                        onDiscoverSeeAllClick = { navController.navigate("discover") }
                    )
                }
            }
        }
    }
}
