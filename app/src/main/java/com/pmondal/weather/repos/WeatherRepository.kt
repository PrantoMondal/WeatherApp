package com.pmondal.weather.repos

import android.location.Location
import com.pmondal.weather.NetworkService
import com.pmondal.weather.models.CurrentModel
import com.pmondal.weather.models.ForecastModel
import com.pmondal.weather.weather_api_key

class WeatherRepository {
    suspend fun fetchCurrentData(location: Location): CurrentModel {
        val endUrl =
            "weather?lat=${location.latitude}&lon=${location.longitude}&units=metric&appid=$weather_api_key"

        return NetworkService.weatherServiceApi
            .getCurrentWeather(endUrl)
    }

    suspend fun fetchForecastData(location: Location): ForecastModel {
        val endUrl =
            "forecast?lat=${location.latitude}&lon=${location.longitude}&units=metric&appid=$weather_api_key"

        return NetworkService.weatherServiceApi
            .getForecastWeather(endUrl)
    }
}