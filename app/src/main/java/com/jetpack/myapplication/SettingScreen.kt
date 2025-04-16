package com.jetpack.myapplication

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.myapplication.datastore.NudgeDataStoreManager
import com.jetpack.myapplication.datastore.NudgeFrequency
import com.jetpack.myapplication.repository.NudgeNotificationRepositoryImpl
import com.jetpack.myapplication.viewmodel.NudgeSettingsViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    // Use a separate view model instance for language preference.
    val languageViewModel: LanguagePreferenceViewModel = viewModel()
    val currentLanguage by languageViewModel.language.collectAsState(initial = "en")
    val context = LocalContext.current

    val nudgeViewModel = remember {
        val manager = NudgeDataStoreManager(context)
        val repo = NudgeNotificationRepositoryImpl(manager)
        NudgeSettingsViewModel(repo, context)
    }


    // State to control the visibility of the language selection dialog.
    var showLanguageDialog by remember { mutableStateOf(false) }

    // Main settings screen UI.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SettingsRow(
                icon = Icons.Default.List,
                title = "Manage Watchlist",
                onClick = { /* Handle watchlist management */ }
            )
            SettingsRow(
                icon = Icons.Default.Notifications,
                title = "Notification Preferences",
                onClick = { /* Handle notification preferences */ }
            )
//            SettingsRowWithToggle(
//                icon = Icons.Default.Notifications,
//                title = "Dark Mode",
//                isChecked = false, // Use your dark mode state here.
//                onCheckedChange = { /* Update dark mode state */ }
//            )
            // Language row: tapping on it shows the language selection dialog.
            SettingsRow(
                icon = Icons.Default.Notifications,
                title = "Language ($currentLanguage)",
                onClick = { showLanguageDialog = true }
            )
            SettingsRow(
                icon = Icons.Default.Delete,
                title = "Clear Cache",
                onClick = { /* Handle clearing the cache */ }
            )
            SettingsRow(
                icon = Icons.Default.Delete,
                title = "Clear History",
                onClick = { /* Handle clearing history */ }
            )
            SettingsRow(
                icon = Icons.Default.LocationOn,
                title = "Location",
                onClick = { /* Handle location settings */ }
            )
            SettingsRow(
                icon = Icons.Default.Star,
                title = "Insights & Statistics",
                onClick = { /* Handle insights view */ }
            )

            Spacer(modifier = Modifier.height(24.dp))
            NudgePreferencesSection(nudgeViewModel)
        }
    }

    // Show the language selection dialog when needed.
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onDismiss = { showLanguageDialog = false },
            onLanguageSelected = { selectedLanguage ->
                languageViewModel.setLanguage(selectedLanguage)
                updateLocale(context, selectedLanguage)
                showLanguageDialog = false
            }
        )
    }
}

// Dialog composable to choose a language.
@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onDismiss: () -> Unit,
    onLanguageSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Language") },
        text = {
            Column {
                // Option for English
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("English", modifier = Modifier.weight(1f))
                    RadioButton(
                        selected = currentLanguage == "en",
                        onClick = { onLanguageSelected("en") }
                    )
                }
                // Option for Spanish
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text("Spanish", modifier = Modifier.weight(1f))
                    RadioButton(
                        selected = currentLanguage == "es",
                        onClick = { onLanguageSelected("es") }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}

// Reusable composable for a settings row.
@Composable
fun SettingsRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.ArrowForward,
            contentDescription = "Go to $title",
            modifier = Modifier.size(24.dp)
        )
    }
}

// Reusable composable for a settings row with a toggle.
@Composable
fun SettingsRowWithToggle(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun NudgePreferencesSection(viewModel: NudgeSettingsViewModel) {
    val context = LocalContext.current
    val prefs by viewModel.nudgePreferences.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var frequency by remember { mutableStateOf(prefs.frequency) }
    var hour by remember { mutableStateOf("") }
    var minute by remember { mutableStateOf("") }

    var hourError by remember { mutableStateOf(false) }
    var minuteError by remember { mutableStateOf(false) }

    val minuteFocusRequester = remember { FocusRequester() }

    LaunchedEffect(prefs) {
        frequency = prefs.frequency
        hour = prefs.hour.toString()
        minute = prefs.minute.toString()
    }

    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
        Text("Nudge Notification Settings", style = MaterialTheme.typography.titleMedium)

        Spacer(Modifier.height(8.dp))
        FrequencyDropdownMenu(frequency) { selected ->
            frequency = selected
        }

        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Hour:")
            Spacer(Modifier.width(4.dp))
            TextField(
                value = hour,
                onValueChange = {
                    hour = it.filter { ch -> ch.isDigit() }
                    hourError = false
                },
                isError = hourError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        minuteFocusRequester.requestFocus()
                    }
                ),
                modifier = Modifier.width(80.dp),
                singleLine = true
            )
            Spacer(Modifier.width(16.dp))
            Text("Minute:")
            Spacer(Modifier.width(4.dp))
            TextField(
                value = minute,
                onValueChange = {
                    minute = it.filter { ch -> ch.isDigit() }
                    minuteError = false
                },
                isError = minuteError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    val hourVal = hour.toIntOrNull()
                    val minuteVal = minute.toIntOrNull()

                    val isHourValid = hourVal in 0..23
                    val isMinuteValid = minuteVal in 0..59

                    if (!isHourValid) hourError = true
                    if (!isMinuteValid) minuteError = true

                    if (isHourValid && isMinuteValid) {
                        viewModel.updateFrequency(frequency)
                        viewModel.updateTime(hourVal!!, minuteVal!!)
                        keyboardController?.hide() // ✅ Dismiss the keyboard
                        Toast.makeText(context, "Nudge settings saved", Toast.LENGTH_SHORT).show()
                    }
                }),
                modifier = Modifier
                    .width(80.dp)
                    .focusRequester(minuteFocusRequester),
                singleLine = true
            )
        }
    }
}


@Composable
fun FrequencyDropdownMenu(
    selected: NudgeFrequency,
    onSelected: (NudgeFrequency) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text("Frequency: ${selected.name}")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            NudgeFrequency.values().forEach {
                DropdownMenuItem(
                    text = { Text(it.name) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}
