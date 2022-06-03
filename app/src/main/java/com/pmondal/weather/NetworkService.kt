package com.pmondal.weather

import com.pmondal.weather.models.CurrentModel
import com.pmondal.weather.models.ForecastModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Url

const val weather_api_key = "b567eba6c40f017dff2045dd2f1914cb"
const val base_url = "https://api.openweathermap.org/data/2.5/"

val retrofit = Retrofit.Builder()
        .baseUrl(base_url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


interface WeatherServiceApi {
        @GET
        suspend fun getCurrentWeather(@Url endUrl: String) : CurrentModel

        @GET
        suspend fun getForecastWeather(@Url endUrl: String) : ForecastModel

}

object NetworkService{
        val weatherServiceApi : WeatherServiceApi by lazy {
                retrofit.create(WeatherServiceApi::class.java)
        }
}