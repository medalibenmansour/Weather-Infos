package com.daly.weatherinfos

import android.app.Application
import com.daly.weatherinfos.depinjection.*
import com.facebook.stetho.Stetho
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this);

        startKoin {
            androidLogger()
            androidContext(this@WeatherApplication)
            //modules(listOf(prefModule, fragmentModule, viewModelModule))
            modules(listOf(prefModule, viewModelModule, networkModule, forecastModule, weatherFragmentModule))
        }
    }
}