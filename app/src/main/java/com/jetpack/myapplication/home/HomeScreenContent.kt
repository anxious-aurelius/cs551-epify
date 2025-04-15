package com.jetpack.myapplication.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.jetpack.myapplication.localDataModels.PopularShowWithPoster
import com.jetpack.myapplication.localDataModels.TrendingShowWithPoster

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onPopularTabSelect: (PopularTab) -> Unit,
    onSearchClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onShowClick: (String) -> Unit,
    onPopularSeeAllClick: () -> Unit,    // ⬅️ NEW: handle "Popular" see all click
    onDiscoverSeeAllClick: () -> Unit    // ⬅️ NEW: handle "Discover" see all click
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Enjoy") },
                actions = {
                    IconButton(onClick = onSearchClicked) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Hero Banner
            item {
                uiState.heroItem?.let { hero ->
                    HeroBanner(hero)
                }
            }

            // Popular Section
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionTitle(
                    title = "Popular",
                    onClick = onPopularSeeAllClick // ⬅️ click navigates
                )
                PopularTabRow(uiState.popularTab, onPopularTabSelect)
                Spacer(modifier = Modifier.height(8.dp))

                HorizontalPopularShowList(
                    shows = when (uiState.popularTab) {
                        PopularTab.TODAY -> uiState.popularToday
                        PopularTab.THIS_WEEK -> uiState.popularThisWeek
                    },
                    onShowClick = onShowClick
                )
            }

            // Discover Section
            if (uiState.discover.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionTitle(
                        title = "Discover",
                        onClick = onDiscoverSeeAllClick // ⬅️ click navigates
                    )
                }
                item {
                    HorizontalTrendingShowList(
                        shows = uiState.discover,
                        onShowClick = onShowClick
                    )
                }
            }
        }
    }
}

@Composable
fun HeroBanner(item: ShowItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Image(
            painter = rememberAsyncImagePainter(item.posterUrl),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = item.title ?: "Unknown Title",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, onClick: () -> Unit) {
    Text(
        text = title,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(onClick = onClick),
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
    )
}

@Composable
fun PopularTabRow(selectedTab: PopularTab, onTabSelect: (PopularTab) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        AssistChip(
            onClick = { onTabSelect(PopularTab.TODAY) },
            label = { Text("Today") },
            modifier = Modifier.padding(end = 8.dp)
        )
        AssistChip(
            onClick = { onTabSelect(PopularTab.THIS_WEEK) },
            label = { Text("This Week") }
        )
    }
}

@Composable
fun HorizontalPopularShowList(
    shows: List<PopularShowWithPoster>,
    onShowClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(shows) { popularShow ->
            ShowCard(
                show = ShowItem(
                    id = popularShow.show.ids.trakt.toString(),
                    title = popularShow.show.title,
                    posterUrl = popularShow.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                ),
                onClick = { onShowClick(popularShow.show.ids.trakt.toString()) }
            )
        }
    }
}

@Composable
fun HorizontalTrendingShowList(
    shows: List<TrendingShowWithPoster>,
    onShowClick: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(shows) { trendingShow ->
            ShowCard(
                show = ShowItem(
                    id = trendingShow.show.ids.trakt.toString(),
                    title = trendingShow.show.title,
                    posterUrl = trendingShow.posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
                ),
                onClick = { onShowClick(trendingShow.show.ids.trakt.toString()) }
            )
        }
    }
}

@Composable
fun ShowCard(show: ShowItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = rememberAsyncImagePainter(show.posterUrl),
            contentDescription = show.title ?: "No title available",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.small)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = show.title ?: "No title available",
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
