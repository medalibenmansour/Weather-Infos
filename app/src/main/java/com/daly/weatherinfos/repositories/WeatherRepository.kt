package com.daly.weatherinfos.repositories

import com.daly.weatherinfos.models.Weather
import com.daly.weatherinfos.network.apis.WeatherApi
import com.daly.weatherinfos.network.utils.Resource
import com.daly.weatherinfos.network.utils.ResponseHandler

open class WeatherRepository(
    private val weatherApi: WeatherApi,
    private val responseHandler: ResponseHandler
) {

    suspend fun getWeather(location: String): Resource<Weather> {
        return try {
            val response = weatherApi.getWeather(location, "metric")
            return responseHandler.handleSuccess(response)
        } catch (e: Exception) {
            responseHandler.handleException(e)
        }
    }
}