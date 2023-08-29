package com.appninjas.data.repository

import com.appninjas.data.network.clients.CryptoApiClient
import com.appninjas.domain.model.Crypto
import com.appninjas.domain.repository.CryptoRepository
import javax.inject.Inject

class CryptoRepoImpl @Inject constructor(private val cryptoApiClient: CryptoApiClient): CryptoRepository {

    override suspend fun getCrypto(): List<Crypto> {
        return cryptoApiClient.getCrypto().toCollection(ArrayList())
    }

}