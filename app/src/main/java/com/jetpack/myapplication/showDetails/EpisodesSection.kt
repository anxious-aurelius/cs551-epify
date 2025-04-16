package com.jetpack.myapplication.showDetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
@Composable
fun EpisodesSection(
    show: ShowDetail,
    onMarkEpisodeWatched: (Episode) -> Unit = {},
    onEpisodeClick: (Episode) -> Unit = {}
) {
    var selectedSeason by remember { mutableStateOf(show.selectedSeason) }
    var showWatched by remember { mutableStateOf("Unwatched") }

    val filteredEpisodes = show.episodeList.filter { episode ->
        episode.seasonNumber == selectedSeason &&
                when (showWatched) {
                    "Unwatched" -> !episode.isWatched
                    "Watched" -> episode.isWatched
                    else -> true
                }
    }

    Column {
        Text(
            text = "Episodes | ${show.title}",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Season selector
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Season:")
            listOf(1, 2).forEach { season ->
                FilterChip(
                    selected = (season == selectedSeason),
                    onClick = { selectedSeason = season },
                    label = { Text(season.toString()) }
                )
            }
        }
        // Watched/Unwatched toggle
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Episodes:")
            FilterChip(
                selected = (showWatched == "Unwatched"),
                onClick = { showWatched = "Unwatched" },
                label = { Text("Unwatched") }
            )
            FilterChip(
                selected = (showWatched == "Watched"),
                onClick = { showWatched = "Watched" },
                label = { Text("Watched") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            filteredEpisodes.forEach { episode ->
                EpisodeItem(
                    episode = episode,
                    onMarkAsWatched = { onMarkEpisodeWatched(episode) },
                    onClick = { onEpisodeClick(episode) }
                )
            }
        }
    }
}
