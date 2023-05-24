package com.example.weatherapp.network
import com.example.weatherapp.api.WeatherService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor

object ApiClient {
    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    //create okhttpclient with logging interceptor. comment this in/out to see API data.
    /*private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

     */
    //retrofit instance with the base URL client and gson conversion.
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //making WeatherService using the instance
    val weatherService: WeatherService by lazy {
        retrofit.create(WeatherService::class.java)
    }
}