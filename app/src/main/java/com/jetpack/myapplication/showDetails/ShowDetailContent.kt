package com.jetpack.myapplication.showDetails
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.jetpack.myapplication.data.WatchedEpisodeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDetailContent(
    show: ShowDetail,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    alreadyInWatchlist: Boolean,
    onAddToWatchlist: (String, String, String) -> Unit,
    onLoadSeason: (Int) -> Unit,
    watchedViewModel: WatchedEpisodeViewModel

) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Create a local mutable copy of show to update its episode list.
    var localShow by remember { mutableStateOf(show) }

    Scaffold(
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ShowBackdropImage(
                backdropUrl = localShow.backdropUrl,
                modifier = Modifier.align(Alignment.TopCenter)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(top = 220.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 4.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Spacer(modifier = Modifier.height(16.dp))
                        ShowTitleSection(title = localShow.title, rating = localShow.rating)
                        Spacer(modifier = Modifier.height(8.dp))
                        GenreChips(genres = localShow.genres)
                        Spacer(modifier = Modifier.height(8.dp))
                        if (alreadyInWatchlist) {
                            Text(
                                text = "Already in Watchlist",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "To manage your watchlist please access it via the settings page.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        } else {
                            Button(
                                onClick = {
                                    onAddToWatchlist(localShow.id.toString(), localShow.title, localShow.posterUrl ?: "")
                                    coroutineScope.launch {
                                        Toast.makeText(context, "Successfully added to watchlist", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Add to Watchlist")
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = localShow.overview,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (localShow.cast.isNotEmpty()) {
                            Text("Cast", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            CastRow(castList = localShow.cast)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (show.episodeList.isNotEmpty()) {
                            EpisodesSection(
                                show = show,
                                onSeasonSelected = onLoadSeason,
                                onMarkEpisodeWatched = { episode ->

                                    watchedViewModel.markAsWatched(
                                        WatchedEpisodeEntity(
                                            episodeId = episode.id,
                                            showId = show.id,
                                            season = episode.seasonNumber,
                                            episode = episode.episodeNumber,
                                            title = episode.title,
                                            isWatched = true
                                        )
                                    )
                                    coroutineScope.launch {
                                        Toast.makeText(context, "Episode marked as watched", Toast.LENGTH_SHORT).show()
                                    }
                                    onLoadSeason(show.selectedSeason)
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                IconButton(onClick = onShareClick) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
                }
            }
        }
    }
}

@Composable
fun ShowBackdropImage(backdropUrl: String?, modifier: Modifier = Modifier) {
    Box(modifier = modifier.height(250.dp)) {
        if (backdropUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(backdropUrl)
                        .build()
                ),
                contentDescription = "Backdrop",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
        )
    }
}

@Composable
fun ShowTitleSection(title: String, rating: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "%.1f ★".format(rating),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun GenreChips(genres: List<String>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        genres.forEach { genre ->
            AssistChip(
                onClick = { /* You might filter by genre later */ },
                label = { Text(genre) }
            )
        }
    }
}

@Composable
fun CastRow(castList: List<CastMember>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        castList.forEach { cast ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(cast.photoUrl)
                            .build()
                    ),
                    contentDescription = cast.name,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cast.name,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EpisodesSection(
    show: ShowDetail,
    onSeasonSelected: (Int) -> Unit,
    onMarkEpisodeWatched: (Episode) -> Unit = {}
) {
    var showWatched by remember { mutableStateOf("Unwatched") }

    val filteredEpisodes = show.episodeList.filter { episode ->
        episode.seasonNumber == show.selectedSeason &&
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
            (1..show.seasons).forEach { season ->
                FilterChip(
                    selected = (season == show.selectedSeason),
                    onClick = { onSeasonSelected(season) },
                    label = { Text("S$season") }
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
                    onMarkAsWatched = { onMarkEpisodeWatched(episode) }
                )
            }
        }
    }
}

@Composable
fun EpisodeItem(episode: Episode) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(episode.imageUrl)
                        .build()
                ),
                contentDescription = episode.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "S${episode.seasonNumber}E${episode.episodeNumber}",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = episode.title,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (episode.isWatched) "Watched" else "Unwatched",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
                )
            }
        }
    }
}
