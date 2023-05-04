package com.example.foundation.models

typealias Mapper<Input, Output> = (Input) -> Output

sealed class Result<T> {

    fun <R> map(mapper: Mapper<T, R>? = null) = when (this) {
        is PendingResult -> PendingResult()
        is ErrorResult -> ErrorResult(this.exception)
        is SuccessResult -> {
            if (mapper == null) throw IllegalArgumentException("Mapper should not be null for SuccessResult")
            SuccessResult(mapper(this.data))
        }
    }

    fun takeSuccess(): T? = if (this is SuccessResult) this.data else null
}

sealed class FinalResult<T> : Result<T>()

class SuccessResult<T>(
    val data: T
) : FinalResult<T>()

class PendingResult<T> : Result<T>()

class ErrorResult<T>(
    val exception: Exception
) : FinalResult<T>()
