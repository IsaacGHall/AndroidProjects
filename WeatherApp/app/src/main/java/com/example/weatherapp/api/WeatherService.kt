package com.example.weatherapp.api
import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    //define API endpoint and req
    @GET("current.json")
    suspend fun getWeather(
        //params
        @Query("q") location: String,
        @Query("key") apiKey: String,
        @Query("aqi") aqi: String
    ): Response<WeatherResponse>
 }