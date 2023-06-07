package com.example.weatherapp.ui.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.api.WeatherService
import com.example.weatherapp.network.ApiClient
import com.example.weatherapp.api.ApiKey.WEATHER_API_KEY
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import java.lang.Exception



class WeatherViewModel : ViewModel() {
    //make network requests
    private val weatherService: WeatherService = ApiClient.weatherService
    //LiveData to hold data
    private val _weatherData = MutableLiveData<WeatherResponse>()
    //val weatherData: MutableLiveData<WeatherResponse> = _weatherData

    private val _temperature = MutableLiveData<Double>()
    val temperature: LiveData<Double> get() = _temperature

    private val _temperatureF = MutableLiveData<Double>()
    val temperatureF: LiveData<Double> get() = _temperatureF

    private val _city = MutableLiveData<String>()
    val cityName: LiveData<String> get() = _city

    private val _region = MutableLiveData<String>()
    val regionName: LiveData<String> get() = _region

    private val _country = MutableLiveData<String>()
    val countryName: LiveData<String> get() = _country

    private val _isDataAvail = MutableLiveData<Boolean>()
    val isDataAvail: LiveData<Boolean> get() = _isDataAvail

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> get() = _longitude


    //allows use as a public function instead of causing private function issues.
    fun fetchWeatherData (location: String){
        viewModelScope.launch {
            fetchWeatherDataInternal(location)
        }
    }

    fun fetchWeatherDataByCoordinates(latitude: Double, longitude: Double) {
        val location = "$latitude,$longitude"
        fetchWeatherData(location)
    }
    //function to fetch API data. is read to console log.
    private suspend fun fetchWeatherDataInternal(location: String) {
        //offload net req from main
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //api params to be changed for user input w/ corrections.
                val apiKey = WEATHER_API_KEY //import your own ApiKey object file with this as its val.
                val aqi = "no"
                val response = weatherService.getWeather(location, apiKey, aqi)

                if (response.isSuccessful) {
                    val weatherResponse = response.body() //parse
                    //you can add logs here for WeatherResponse
                    //if not null then post ->
                    weatherResponse?.let {
                        //(you can also log data here)
                        _weatherData.postValue(it) //here
                        Log.d("City and Country: ", "${it.location.cityName}, ${it.location.countryName}")
                        _temperature.postValue(it.currentWeather.temperatureCelsius) //here
                        _temperatureF.postValue(it.currentWeather.temperatureFahrenheit)
                        _city.postValue(it.location.cityName) //here
                        _region.postValue(it.location.regionName)
                        _country.postValue(it.location.countryName) //and here
                        _isDataAvail.postValue(true)
                        _latitude.postValue(it.location.latitude)
                        _longitude.postValue(it.location.longitude)
                        Log.d("GPS Coords: ", "${it.location.latitude}, ${it.location.longitude}")

                    }

                } else {
                    Log.e("API ERROR", response.message()) //error
                    _isDataAvail.postValue(false)
                }
            } catch (e: Exception){
                //remember 200 is good
                e.printStackTrace()
            }

        }

    }
}