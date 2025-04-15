package com.jetpack.myapplication.showList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jetpack.myapplication.data.HomeRepository
import com.jetpack.myapplication.data.TMDBRepository
import com.jetpack.myapplication.home.ShowCard
import com.jetpack.myapplication.home.ShowItem

@Composable
fun DiscoverShowsScreen(
    onShowClick: (String) -> Unit
){
    var discoverShows by remember { mutableStateOf<List<ShowItem>>(emptyList()) }
    var page by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }

    val listState = rememberLazyGridState()

    LaunchedEffect(page) {
        isLoading = true
        val repo = HomeRepository()
        val tmdbRepo = TMDBRepository()

        val shows = repo.fetchTrendingShows(page = page)

        val showItems = shows.map { show ->
            val posterPath = show.show.ids.tmdb?.let { tmdbId ->
                tmdbRepo.getPosterPath(tmdbId)
            }
            ShowItem(
                id = show.show.ids.trakt.toString(),
                title = show.show.title,
                posterUrl = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
            )
        }
        discoverShows = discoverShows + showItems
        isLoading = false
    }

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(discoverShows.size) { index ->
            ShowCard(
                show = discoverShows[index],
                onClick = { onShowClick(discoverShows[index].id ?: "") }
            )
        }

//        item {
//            if (isLoading) {
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
//            }
//        }
    }

    // Load next page when reaching near the end
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == discoverShows.lastIndex) {
                    page++
                }
            }
    }
}
