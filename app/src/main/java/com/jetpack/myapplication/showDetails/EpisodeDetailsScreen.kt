package com.jetpack.myapplication.showDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.myapplication.data.EpisodeDao

@Composable
fun EpisodeDetailsScreen(episodeId: Int, episodeDao: EpisodeDao) {
    val viewModel: EpisodeDetailsViewModel = viewModel(
        factory = EpisodeDetailsViewModelFactory(episodeDao, episodeId)
    )
    val episode = viewModel.episode

    episode?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("S${it.season}E${it.episode}: ${it.title}", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            Button(onClick = { viewModel.toggleWatchedStatus() }) {
                Text(if (it.isWatched) "Mark as Unwatched" else "Mark as Watched")
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
