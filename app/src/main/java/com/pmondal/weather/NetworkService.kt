package com.pmondal.weather

import com.pmondal.weather.models.CurrentModel
import com.pmondal.weather.models.ForecastModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Url
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

const val weather_api_key = "b567eba6c40f017dff2045dd2f1914cb"
const val base_url = "https://api.openweathermap.org/data/2.5/"
const val icon_prefix = "https://openweathermap.org/img/wn/"
const val icon_suffix = "@2x.png"

fun getFormattedDate(dt:Long,pattern: String): String {
        return SimpleDateFormat(pattern).format(Date(dt*1000))
}

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