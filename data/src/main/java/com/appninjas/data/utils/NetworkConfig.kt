package com.appninjas.data.utils

object NetworkConfig {

    const val BASE_URL = "http://89.108.76.73:4000/"

    object Weather {
        object Geocoder {
            const val GET_ADDRESS_ROUTE = "geocode/getAddress"
        }

        const val GET_WEATHER_ROUTE = "weather/get"
    }

    object Crypto {
        const val GET_CRYPTO_ROUTE = "crypto/get"
    }

}