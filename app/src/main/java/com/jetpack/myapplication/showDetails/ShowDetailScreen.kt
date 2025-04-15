package com.jetpack.myapplication.showDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ShowDetailScreen(
    showId: String,
    onBackClick: () -> Unit = {},
    onShareClick: () -> Unit = {}
) {
    val viewModel: ShowDetailViewModel = viewModel()

    LaunchedEffect(showId) {
        viewModel.loadShowDetails(showId)
    }

    val showDetail by remember { viewModel.showDetail }

    if (showDetail == null) {
        // Loading state
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        // Loaded: show the content
        ShowDetailContent(
            show = showDetail!!,
            onBackClick = onBackClick,
            onShareClick = onShareClick
        )
    }
}
