package com.example.otchallenge.service

import com.example.otchallenge.models.BookResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Api Service class that takes in the api-key as query into the URL
 */
interface BookApiService {
    @GET("lists/current/hardcover-fiction.json")
    fun getBooks(
        @Query("api-key") apiKey: String = "KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB",
        @Query("offset") offset: Int = 0,
    ): Deferred<BookResponse>
}