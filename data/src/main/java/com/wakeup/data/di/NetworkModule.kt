package com.wakeup.data.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.wakeup.data.BuildConfig
import com.wakeup.data.network.response.WeatherContainerResponse
import com.wakeup.data.network.response.WeatherResponse
import com.wakeup.data.network.service.ImageSearchService
import com.wakeup.data.network.service.PlaceSearchService
import com.wakeup.data.network.service.WeatherService
import com.wakeup.domain.model.WeatherType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    @Named("Kakao")
    fun provideKakaoInterceptor(): Interceptor {
        return Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", BuildConfig.KAKAO_REST_API_KEY)
                    .build()
            )
        }
    }

    @Provides
    @Singleton
    @Named("Kakao")
    fun provideKakaoOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("Kakao") kakaoHeaderInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(kakaoHeaderInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    @Provides
    @Singleton
    @Named("Weather")
    fun provideWeatherInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("appid", BuildConfig.WEATHER_API_KEY)
                .build()
            chain.proceed(
                request.newBuilder()
                    .url(url)
                    .build()
            )
        }
    }

    @Provides
    @Singleton
    @Named("Filtering")
    fun provideFilteringInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            val jsonString = response.body?.string() ?: ""
            val json = Json { ignoreUnknownKeys = true }
            val result = json.decodeFromString<WeatherContainerResponse>(jsonString)
            val weather = result.weathers.first()
            val type = getWeatherTypeById(weather.id)
            val main = result.main
            response.newBuilder()
                .message(response.message)
                .body(
                    Json.encodeToString(
                        WeatherResponse(
                            weather.id,
                            type,
                            weather.icon,
                            main.temperature
                        )
                    ).toResponseBody()
                )
                .build()
        }
    }

    private fun getWeatherTypeById(id: Long): WeatherType {
        return when (id) {
            in 200..299 -> WeatherType.THUNDERSTORM
            in 300..399 -> WeatherType.DRIZZLE
            in 500..599 -> WeatherType.RAIN
            in 600..699 -> WeatherType.SNOW
            in 700..799 -> WeatherType.ATMOSPHERE
            in 800..899 -> WeatherType.CLOUDS
            else -> WeatherType.CLEAR
        }
    }

    @Provides
    @Singleton
    @Named("Weather")
    fun provideWeatherOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @Named("Weather") weatherHeaderInterceptor: Interceptor,
        @Named("Filtering") filteringInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(weatherHeaderInterceptor)
            .addInterceptor(filteringInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        val json = Json { ignoreUnknownKeys = true }
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    @Named("Kakao")
    fun provideKakaoRetrofit(
        @Named("Kakao") okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    @Named("Weather")
    fun provideWeatherRetrofit(
        @Named("Weather") okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.WEATHER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun providePlaceSearchService(@Named("Kakao") retrofit: Retrofit): PlaceSearchService {
        return retrofit.create(PlaceSearchService::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherService(@Named("Weather") retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    @Singleton
    fun provideImageSearchService(@Named("Kakao") retrofit: Retrofit): ImageSearchService {
        return retrofit.create(ImageSearchService::class.java)
    }
}