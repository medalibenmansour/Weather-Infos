package com.daly.weatherinfos.viewmodels

import androidx.lifecycle.*
import com.daly.weatherinfos.network.utils.Resource
import com.daly.weatherinfos.repositories.WeatherRepository
import kotlinx.coroutines.Dispatchers

class WeatherViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {

    private val location = MutableLiveData<String>()

    fun getWeather(input: String) {
        location.value = input
    }

    var weather = location.switchMap { location ->
        liveData(Dispatchers.IO) {
            emit(Resource.loading(null))
            emit(weatherRepo.getWeather(location))
        }
    }
}