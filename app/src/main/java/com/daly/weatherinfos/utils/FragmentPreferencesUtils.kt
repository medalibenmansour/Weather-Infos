package com.daly.weatherinfos.utils

import android.content.Context

const val SHOW_FRAGMENT_KEY = "SHOW_FRAGMENT_KEY"


class FragmentPreferencesUtils(context: Context) {

    private val preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun storeShouldShowFragment(shouldShow: Boolean) {
        preferences.edit().putBoolean(SHOW_FRAGMENT_KEY, shouldShow).apply()
    }

    fun getShouldShowFragment() = preferences.getBoolean(SHOW_FRAGMENT_KEY, false)
}