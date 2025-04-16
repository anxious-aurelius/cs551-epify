package com.jetpack.myapplication.navigationBar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.jetpack.myapplication.SettingsScreen
import com.jetpack.myapplication.home.HomeScreenContent
import com.jetpack.myapplication.home.HomeViewModel
import com.jetpack.myapplication.notification.NotificationsScreen
import com.jetpack.myapplication.showDetails.ShowDetailScreen
import com.jetpack.myapplication.showList.DiscoverShowsScreen
import com.jetpack.myapplication.watchlist.WatchlistScreen
import java.net.URLEncoder

@Composable
fun MainScreenWithCustomBottomBar(navController: NavController) {
    val bottomBarItems = listOf(
        BottomBarItem(route = "home", label = "Home", icon = Icons.Default.Home),
        BottomBarItem(route = "watchlist", label = "Watchlist", icon = Icons.Default.CheckCircle),
        BottomBarItem(route = "notification", label = "Notification", icon = Icons.Default.Notifications),
        BottomBarItem(route = "account", label = "Account", icon = Icons.Default.Person)
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home"

    NavigationBar {
        bottomBarItems.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    // Only navigate if we're not already on the selected route.
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
