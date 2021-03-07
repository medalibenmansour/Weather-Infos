package com.daly.weatherinfos.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.daly.weatherinfos.R
import com.daly.weatherinfos.databinding.FragmentWeatherBinding
import com.daly.weatherinfos.models.TempData
import com.daly.weatherinfos.models.Weather
import com.daly.weatherinfos.network.utils.Resource
import com.daly.weatherinfos.network.utils.Status
import com.daly.weatherinfos.viewmodels.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {

    private val weatherViewModel: WeatherViewModel by viewModel()
    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        binding.viewModel = weatherViewModel
        weatherViewModel.weather.observe(viewLifecycleOwner, observer)
        return binding.root
    }

    private val observer = Observer<Resource<Weather>> {
        when (it.status) {
            Status.SUCCESS -> updateTemperatureText(it.data!!.name, it.data.temp)
            Status.ERROR -> showError(it.message!!)
            Status.LOADING -> showLoading()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showLoading() {
        binding.weatherInfo.setTextColor(Color.BLACK)
        binding.weatherInfo.text = "Loading..."
    }

    @SuppressLint("SetTextI18n")
    private fun showError(message: String) {
        binding.weatherInfo.setTextColor(Color.RED)
        binding.weatherInfo.text = "Error: $message"
    }

    @SuppressLint("SetTextI18n")
    private fun updateTemperatureText(name: String, temp: TempData) {
        binding.weatherInfo.setTextColor(Color.BLACK)
        binding.weatherInfo.text = String.format("Temperature at %s is %s celcius", name, temp.temp)
    }
}