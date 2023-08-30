package com.appninjas.data.repository

import com.appninjas.data.network.clients.CryptoApiClient
import com.appninjas.domain.model.Crypto
import com.appninjas.domain.repository.CryptoRepository
import com.appninjas.domain.utils.Response
import retrofit2.HttpException
import java.net.ConnectException
import javax.inject.Inject

class CryptoRepoImpl @Inject constructor(private val cryptoApiClient: CryptoApiClient): CryptoRepository {

    override suspend fun getCrypto(): Response<List<Crypto>> {
        return try {
            val networkResponse = cryptoApiClient.getCrypto()
            Response.Success.Data(networkResponse.toCollection(ArrayList()))
        } catch(e: HttpException) {
            when (e.code()) {
                404 -> Response.Error("Ошибка сети: страница не найдена. Попробуйте позже")
                500 -> Response.Error("Ошибка сервера. Попробуйте зайти позже")
                403 -> Response.Error("Ошибка сети. Скорее всего исчерпан лимит запросов к API сервиса погоды. Попробуйте зайти завтра")
                else -> Response.Error("Неизвестная ошибка сети")
            }
        } catch(e: ConnectException) {
            Response.Error("Не удалось подключится к серверу. Проверьте качество соединения с сетью")
        } finally {
            Response.Success.Empty
        }
    }

}