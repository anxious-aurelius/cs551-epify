package com.jetpack.myapplication.notification
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen() {
    // Sample data for demonstration
    val upcomingEpisodes = listOf(
        Episode(
            showName = "Paradise",
            seasonNumber = 2,
            episodeNumber = 8,
            rating = 8.9,
            releaseInfo = "In 3 days",
            overview = "Lisa’s attempt to find the hidden truth leads her to a shocking discovery.",
            imageUrl = "https://placehold.co/100x150",
            isReleased = false
        ),
        Episode(
            showName = "Paradise",
            seasonNumber = 2,
            episodeNumber = 9,
            rating = 8.7,
            releaseInfo = "In 5 days",
            overview = "Drama intensifies as the group splits on a critical mission.",
            imageUrl = "https://placehold.co/100x150",
            isReleased = false
        )
    )

    val recentlyReleasedEpisodes = listOf(
        Episode(
            showName = "Paradise",
            seasonNumber = 2,
            episodeNumber = 7,
            rating = 8.9,
            releaseInfo = "Released 2 days ago",
            overview = "New alliances form while old tensions flare.",
            imageUrl = "https://placehold.co/100x150",
            isReleased = true
        )
    )

    val scheduledNotifications = listOf(
        Episode(
            showName = "Paradise",
            seasonNumber = 2,
            episodeNumber = 10,
            rating = 9.0,
            releaseInfo = "In 1 week",
            overview = "Season finale: who will truly make it out unscathed?",
            imageUrl = "https://placehold.co/100x150",
            isReleased = false
        )
    )

    // Main layout
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Notifications") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header text
            item {
                Text(
                    text = "Stay updated with new episodes and recommendations.",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Upcoming Episodes
            if (upcomingEpisodes.isNotEmpty()) {
                item {
                    Text(
                        text = "Upcoming Episode Alerts",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(upcomingEpisodes) { episode ->
                    EpisodeCard(episode)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Recently Released
            if (recentlyReleasedEpisodes.isNotEmpty()) {
                item {
                    Text(
                        text = "Recently Released Episodes",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(recentlyReleasedEpisodes) { episode ->
                    EpisodeCard(episode)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Scheduled Notifications
            if (scheduledNotifications.isNotEmpty()) {
                item {
                    Text(
                        text = "Scheduled Notifications",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                items(scheduledNotifications) { episode ->
                    EpisodeCard(episode)
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}
