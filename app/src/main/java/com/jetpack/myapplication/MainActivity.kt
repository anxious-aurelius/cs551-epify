package com.jetpack.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.myapplication.navigationBar.AppNavigation
import com.jetpack.myapplication.navigationBar.MainScreenWithCustomBottomBar
import com.jetpack.myapplication.ui.theme.MyApplicationTheme




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppNavigation()
            }
        }
    }
}



@Composable
fun LanguagePreferenceComponent() {
    val viewModel: LanguagePreferenceViewModel = viewModel() // Get ViewModel here
    // Observe the current language from the ViewModel
    val language by viewModel.language.collectAsState(initial = "en")
    // No initial value needed
    val context = LocalContext.current
    Column {
        Text(text = "Current language: $language")
        Button(onClick = {
            // Toggle between "en" and "es" for demonstration
            val newLanguage = if (language == "en") "es" else "en"
            viewModel.setLanguage(newLanguage)
            updateLocale(context, newLanguage)
        }) {
            Text("Toggle Language")
        }
    }
}
