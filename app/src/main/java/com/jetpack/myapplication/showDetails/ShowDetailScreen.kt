package com.jetpack.myapplication.showDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.jetpack.myapplication.api.TraktService
import com.jetpack.myapplication.application.RetrofitClient
import com.jetpack.myapplication.data.*
import com.jetpack.myapplication.watchlist.WatchlistViewModel
import com.jetpack.myapplication.watchlist.WatchlistViewModelFactory

@Composable
fun ShowDetailScreen(
    showId: String,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onEpisodeClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val db = WatchlistDatabase.getDatabase(context)

    val showDetailRepo = ShowDetailRepository(
        tmdbRepository = TMDBRepository(),
        traktService = RetrofitClient.instance.create(TraktService::class.java),
        episodeDao = db.episodeDao()
    )
    val viewModel: ShowDetailViewModel = viewModel(
        factory = ShowDetailViewModelFactory(showDetailRepo)
    )

    val watchlistRepo = WatchlistRepository(db.watchlistDao())
    val watchlistViewModel: WatchlistViewModel = viewModel(
        factory = WatchlistViewModelFactory(watchlistRepo)
    )

    val episodeRepo = WatchedEpisodeRepository(db.episodeDao())
    val watchedViewModel: WatchedEpisodeViewModel = viewModel(
        factory = WatchedEpisodeViewModelFactory(episodeRepo)
    )

    LaunchedEffect(showId) {
        viewModel.loadShowDetails(showId)
    }

    val watchlist by watchlistViewModel.watchlist.collectAsState()
    val showDetail = viewModel.showDetail.value

    var addedLocally by remember { mutableStateOf(false) }

    if (showDetail == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val alreadyInWatchlist = watchlist.any { it.id == showId } || addedLocally

        ShowDetailContent(
            show = showDetail,
            onBackClick = onBackClick,
            onShareClick = onShareClick,
            alreadyInWatchlist = alreadyInWatchlist,
            onAddToWatchlist = { id, title, posterUrl ->
                val item = WatchlistEntity(id = showId, title = title, posterUrl = posterUrl)
                watchlistViewModel.addItem(item)
                addedLocally = true
            },
            onLoadSeason = { season ->
                viewModel.loadSeasonEpisodes(season)
            },
            watchedViewModel = watchedViewModel,
            onEpisodeClick = { episode ->

                onEpisodeClick(episode.id)
            }
        )

    }}
