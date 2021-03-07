package com.daly.weatherinfos.network.apis

import com.daly.weatherinfos.models.Weather
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather")
    suspend fun getWeather(@Query("q")location: String,
                            @Query("units") unit: String): Weather
}