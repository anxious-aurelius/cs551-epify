package com.jetpack.myapplication.notification
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star


@Composable
fun EpisodeCard(episode: Episode) {
    // Use a card to contain the episode info
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Episode Image


            Spacer(modifier = Modifier.width(12.dp))

            // Episode details
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row: Title & rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "${episode.showName} (season-${episode.seasonNumber})",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color(0xFFFFD700), // Gold color
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${episode.rating}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Episode number + release info
                Text(
                    text = "Episode - ${episode.episodeNumber} | ${episode.releaseInfo}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )

                // Overview
                Text(
                    text = episode.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Action buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { /* Handle notify me logic */ }) {
                        Text("Notify me")
                    }
                    if (episode.isReleased) {
                        Button(onClick = { /* Handle watch now logic */ }) {
                            Text("Watch now")
                        }
                    }
                    Button(onClick = { /* Handle add to watchlist logic */ }) {
                        Text("Add to watch list")
                    }
                }
            }
        }
    }
}
