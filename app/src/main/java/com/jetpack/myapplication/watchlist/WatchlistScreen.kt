package com.jetpack.myapplication.watchlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.jetpack.myapplication.data.WatchlistEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel,
    onShowClick: (String) -> Unit
) {
    // Collect state from the ViewModel
    val watchlist by viewModel.watchlist.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Your Watchlist") })
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(
                items = watchlist,
                key = { item -> item.id } // Provide a stable unique key
            ) { item ->
                WatchlistItem(item = item, onClick = { onShowClick(item.id) })
            }
        }
    }
}

@Composable
fun WatchlistItem(item: WatchlistEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = item.posterUrl),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = item.title, style = MaterialTheme.typography.titleMedium)
    }
}
