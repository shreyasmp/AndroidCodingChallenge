package com.example.otchallenge.util

sealed class ResultWrapper<T> {
    data class Loading<T>(val isLoading: Boolean) : ResultWrapper<T>()
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Failure<T>(val throwable: Throwable) : ResultWrapper<T>()
}