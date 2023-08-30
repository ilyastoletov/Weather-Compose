package com.appninjas.domain.repository

import com.appninjas.domain.model.Crypto
import com.appninjas.domain.utils.Response

interface CryptoRepository {
    suspend fun getCrypto(): Response<List<Crypto>>
}