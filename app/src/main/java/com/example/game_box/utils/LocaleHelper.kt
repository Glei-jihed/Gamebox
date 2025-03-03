package com.example.game_box.utils

import android.app.Activity
import android.content.res.Configuration
import java.util.Locale

object LocaleHelper {
    fun setLocale(activity: Activity, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(activity.resources.configuration)
        config.setLocale(locale)
        activity.resources.updateConfiguration(config, activity.resources.displayMetrics)
        // Recrée l'activité pour appliquer la nouvelle langue
        activity.recreate()
    }
}
