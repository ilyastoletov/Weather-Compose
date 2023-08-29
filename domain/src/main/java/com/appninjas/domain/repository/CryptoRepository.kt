package com.appninjas.domain.repository

import com.appninjas.domain.model.Crypto

interface CryptoRepository {
    suspend fun getCrypto(): List<Crypto>
}