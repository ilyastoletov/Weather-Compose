package com.appninjas.composeweather_2.presentation.di

import com.appninjas.domain.repository.WeatherRepository
import com.appninjas.domain.usecase.weather.GeocodeCoordinatesUseCase
import com.appninjas.domain.usecase.weather.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
class DomainModule {

    @Provides
    fun provideGeocodeCoordinatesUseCase(weatherRepository: WeatherRepository): GeocodeCoordinatesUseCase {
        return GeocodeCoordinatesUseCase(weatherRepository)
    }

    @Provides
    fun provideGetWeatherUseCase(weatherRepository: WeatherRepository): GetWeatherUseCase {
        return GetWeatherUseCase(weatherRepository)
    }

}