package com.jetpack.myapplication.home


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.myapplication.application.Show
import com.jetpack.myapplication.application.TrendingShow

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val trendingShows by viewModel.trendingShows.collectAsState()
    val popularShows by viewModel.popularShows.collectAsState()


    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Trending Shows", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(trendingShows) { show ->
                ShowTrendingItemCard(show)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Popular Shows", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(popularShows) { show ->
                ShowPopularItemCard(show)
            }
        }
    }
}

@Composable
fun ShowTrendingItemCard(show: TrendingShow) {
    Card(modifier = Modifier.width(150.dp).padding(end = 8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {

            Text(text = show.show.title, style = MaterialTheme.typography.titleMedium)
            Text(text = show.show.year.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}
@Composable
fun ShowPopularItemCard(show: Show) {
    Card(modifier = Modifier.width(150.dp).padding(end = 8.dp)) {
        Column(modifier = Modifier.padding(8.dp)) {

            Text(text = show.title, style = MaterialTheme.typography.titleMedium)
            Text(text = show.year.toString(), style = MaterialTheme.typography.bodySmall)
        }
    }
}