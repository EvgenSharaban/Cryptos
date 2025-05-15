package com.example.cryptos.core.networking

import android.util.Log
import com.example.cryptos.core.other.TAG
import com.example.cryptos.data.network.entities.mappers.FromEntityToDomainMapper
import retrofit2.Response


suspend fun <T> safeCall(
    action: suspend () -> Response<T>
): Result<T> {
    return try {
        val response = action.invoke()
        ApiResponseHandler.DefaultApiResponseHandler<T>().handleApiResponse(response)
    } catch (e: Throwable) {
        Log.e(TAG, "error during request: $e", e)
        Result.failure(e)
    }
}

fun <R, T> Result<T>.toDomain(mapper: FromEntityToDomainMapper<T, R>): Result<R> {
    return this.map { value ->
        mapper.mapToDomain(value)
    }
}