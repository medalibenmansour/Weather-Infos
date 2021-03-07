package com.daly.weatherinfos.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.daly.weatherinfos.R
import com.daly.weatherinfos.utils.FragmentPreferencesUtils
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val preferences: FragmentPreferencesUtils by inject()
    private val weatherFragment: WeatherFragment by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferences.storeShouldShowFragment(true)
    }

    override fun onResume() {
        super.onResume()
        if (preferences.getShouldShowFragment()) {
            supportFragmentManager.beginTransaction().replace(R.id.root, weatherFragment, "weather").commit()
        }
    }
}