package com.daly.weatherinfos.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.daly.weatherinfos.models.TempData
import com.daly.weatherinfos.models.Weather
import com.daly.weatherinfos.network.utils.Resource
import com.daly.weatherinfos.repositories.WeatherRepository
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.*
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WeatherViewModelTest {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var weatherObserver: Observer<Resource<Weather>>
    private val validLocation = "Helsinki"
    private val invalidLocation = "Helsinkii"
    private val successResource = Resource.success(Weather(TempData(1.0, 1), "test"))
    private val errorResource = Resource.fail("Unauthorised", null)

    /**
     * Important for JUNIT4 to test live data
     */
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ObsoleteCoroutinesApi
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        weatherRepository = mock()
        runBlocking {
            whenever(weatherRepository.getWeather(validLocation)).thenReturn(successResource)
            whenever(weatherRepository.getWeather(invalidLocation)).thenReturn(errorResource)
        }
        weatherViewModel = WeatherViewModel(weatherRepository)
        weatherObserver = mock()
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun `when getWeather is called with valid location, then observer is updated with success`() = runBlocking {
        weatherViewModel.weather.observeForever(weatherObserver)
        weatherViewModel.getWeather(validLocation)
        delay(10)
        verify(weatherRepository).getWeather(validLocation)
        verify(weatherObserver, timeout(50)).onChanged(Resource.loading(null))
        verify(weatherObserver, timeout(50)).onChanged(successResource)
    }

    @Test
    fun `when getWeather is called with invalid location, then observer is updated with failure`() = runBlocking {
        weatherViewModel.weather.observeForever(weatherObserver)
        weatherViewModel.getWeather(invalidLocation)
        delay(10)
        verify(weatherRepository).getWeather(invalidLocation)
        verify(weatherObserver, timeout(50)).onChanged(Resource.loading(null))
        verify(weatherObserver, timeout(50)).onChanged(errorResource)
    }
}