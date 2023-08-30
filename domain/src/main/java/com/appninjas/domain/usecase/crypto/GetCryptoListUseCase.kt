package com.appninjas.domain.usecase.crypto

import com.appninjas.domain.repository.CryptoRepository

class GetCryptoListUseCase(private val repository: CryptoRepository) {
    suspend fun invoke() = repository.getCrypto()
}