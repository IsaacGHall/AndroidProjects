package com.example.weatherapp.model
import com.google.gson.annotations.SerializedName
data class WeatherResponse (
    @SerializedName("current")
    val currentWeather: CurrentWeather, //curr weather data from api
    val location : Location
)

data class Location(
    @SerializedName("name") //city
    val cityName: String,

    @SerializedName("country") //country
    val countryName: String,

    @SerializedName("region") //region
    val region: String
)

data class CurrentWeather(
    @SerializedName("temp_c") //temp in c
    val temperatureCelsius: Double,

    @SerializedName("condition") //to be used later, weather conditions
    val condition: Condition,

    @SerializedName("wind_kph") //to be used later
    val windSpeedKph: Double,

    @SerializedName("pressure_mb") //'', millibars
    val pressureMb: Double,

    @SerializedName("humidity") //'', humidity
    val humidity: Int,
)
data class Condition (
    @SerializedName("text") //'',condition description
    val text: String,

    @SerializedName("icon") //'',url to weather icon
    val iconUrl: String,

    @SerializedName("code") //'',condition code
    val code: Int
)