package com.appninjas.data.network.clients

import com.appninjas.data.utils.NetworkConfig
import com.appninjas.domain.model.Crypto
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptoApiClient {

    @GET(NetworkConfig.Crypto.GET_CRYPTO_ROUTE)
    suspend fun getCrypto(@Query("vs_currency") currency: String = "usd"): Array<Crypto>

}