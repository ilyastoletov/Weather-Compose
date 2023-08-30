package com.appninjas.data.di

import com.appninjas.data.network.clients.CryptoApiClient
import com.appninjas.data.network.clients.WeatherApiClient
import com.appninjas.data.repository.CryptoRepoImpl
import com.appninjas.data.repository.WeatherRepoImpl
import com.appninjas.data.utils.NetworkConfig
import com.appninjas.domain.repository.CryptoRepository
import com.appninjas.domain.repository.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(NetworkConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideWeatherApiClient(retrofit: Retrofit): WeatherApiClient = retrofit.create(WeatherApiClient::class.java)

    @Provides
    @Singleton
    fun provideCryptoApiClient(retrofit: Retrofit): CryptoApiClient = retrofit.create(CryptoApiClient::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(weatherApiClient: WeatherApiClient): WeatherRepository {
        return WeatherRepoImpl(weatherApiClient)
    }

    @Provides
    @Singleton
    fun provideCryptoRepository(cryptoApiClient: CryptoApiClient): CryptoRepository {
        return CryptoRepoImpl(cryptoApiClient)
    }

}