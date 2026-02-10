package com.nisr.sau.utils

import android.content.Context
import android.content.SharedPreferences

object SettingsManager {
    private const val PREFS_NAME = "sau_prefs"
    private const val ONBOARDING_COMPLETE = "onboarding_complete"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isOnboardingComplete(context: Context): Boolean {
        return getPrefs(context).getBoolean(ONBOARDING_COMPLETE, false)
    }

    fun setOnboardingComplete(context: Context, isComplete: Boolean) {
        getPrefs(context).edit().putBoolean(ONBOARDING_COMPLETE, isComplete).apply()
    }
}
