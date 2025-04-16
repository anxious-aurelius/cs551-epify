package com.jetpack.myapplication.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.jetpack.myapplication.data.TMDBRepository
import com.jetpack.myapplication.data.TmdbGenre
import com.jetpack.myapplication.data.TmdbShow
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdvancedSearchScreen(
    onBackClick: () -> Unit,
    onShowClick: (Int) -> Unit
) {
    val repository = TMDBRepository()
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var includeAdult by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("en-US") }
    val languages = listOf("en-US", "es-ES", "fr-FR", "hi-IN", "ja-JP")

    var searchResults by remember { mutableStateOf<List<TmdbShow>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    var genres by remember { mutableStateOf<List<TmdbGenre>>(emptyList()) }
    var selectedGenre by remember { mutableStateOf<TmdbGenre?>(null) }

    var sortBy by remember { mutableStateOf("Name A-Z") }
    val sortOptions = listOf("Name A-Z", "Name Z-A", "Newest First", "Oldest First")

    var genreExpanded by remember { mutableStateOf(false) }
    var sortExpanded by remember { mutableStateOf(false) }
    var langExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        genres = repository.getGenres()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Advanced Search") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Show Title") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = year,
                            onValueChange = { year = it },
                            label = { Text("Year (optional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Genre Dropdown
                        ExposedDropdownMenuBox(
                            expanded = genreExpanded,
                            onExpandedChange = { genreExpanded = it }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedGenre?.name ?: "Select Genre",
                                onValueChange = {},
                                label = { Text("Genre") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = genreExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = genreExpanded,
                                onDismissRequest = { genreExpanded = false }
                            ) {
                                genres.forEach { genre ->
                                    DropdownMenuItem(
                                        text = { Text(genre.name) },
                                        onClick = {
                                            selectedGenre = genre
                                            genreExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Sort Dropdown
                        ExposedDropdownMenuBox(
                            expanded = sortExpanded,
                            onExpandedChange = { sortExpanded = it }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = sortBy,
                                onValueChange = {},
                                label = { Text("Sort By") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = sortExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = sortExpanded,
                                onDismissRequest = { sortExpanded = false }
                            ) {
                                sortOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            sortBy = option
                                            sortExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Adult Toggle
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Include Adult")
                            Spacer(modifier = Modifier.width(8.dp))
                            Switch(checked = includeAdult, onCheckedChange = { includeAdult = it })
                        }

                        // Language Dropdown
                        ExposedDropdownMenuBox(
                            expanded = langExpanded,
                            onExpandedChange = { langExpanded = it }
                        ) {
                            OutlinedTextField(
                                readOnly = true,
                                value = selectedLanguage,
                                onValueChange = {},
                                label = { Text("Language") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = langExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = langExpanded,
                                onDismissRequest = { langExpanded = false }
                            ) {
                                languages.forEach { lang ->
                                    DropdownMenuItem(
                                        text = { Text(lang) },
                                        onClick = {
                                            selectedLanguage = lang
                                            langExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isLoading = true
                                    val parsedYear = year.toIntOrNull()
                                    val rawResults = repository.searchShows(
                                        title = title,
                                        year = parsedYear,
                                        includeAdult = includeAdult,
                                        language = selectedLanguage
                                    )
                                    var filtered = rawResults
                                    selectedGenre?.let {
                                        filtered = filtered.filter { show -> show.genreIds.contains(it.id) }
                                    }
                                    filtered = when (sortBy) {
                                        "Name A-Z" -> filtered.sortedBy { it.name }
                                        "Name Z-A" -> filtered.sortedByDescending { it.name }
                                        "Newest First" -> filtered.sortedByDescending { it.firstAirDate }
                                        "Oldest First" -> filtered.sortedBy { it.firstAirDate }
                                        else -> filtered
                                    }
                                    searchResults = filtered
                                    isLoading = false
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Search")
                        }
                    }
                }
            }

            item {
                Divider()
                Text("Search Results", style = MaterialTheme.typography.titleMedium)
            }

            when {
                isLoading -> {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }

                searchResults.isEmpty() -> {
                    item {
                        Text("No results found.")
                    }
                }

                else -> {
                    items(searchResults) { show ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onShowClick(show.id) },
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                val posterUrl = "https://image.tmdb.org/t/p/w500${show.posterPath}"
                                AsyncImage(
                                    model = posterUrl,
                                    contentDescription = show.name,
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(150.dp)
                                        .clip(MaterialTheme.shapes.medium),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(show.name, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        text = show.overview ?: "No description available.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 4,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    if (!show.firstAirDate.isNullOrBlank()) {
                                        Text(
                                            text = "First aired: ${show.firstAirDate}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
