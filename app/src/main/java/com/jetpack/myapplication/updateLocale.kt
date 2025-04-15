package com.jetpack.myapplication

import android.content.Context
import java.util.Locale

fun updateLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)
    val resources = context.resources
    val configuration = resources.configuration
    configuration.setLocale(locale)
    // Optionally recreate configuration context or activity to reflect changes
    context.createConfigurationContext(configuration)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}
