package com.example.otchallenge.util

import com.example.otchallenge.models.Book
import com.example.otchallenge.models.BookResponse
import com.example.otchallenge.models.BookResults

fun provideBook1()  = Book(title = "book1", description = "description1", book_image = "image1")

fun provideBook2()  = Book(title = "book2", description = "description2", book_image = "image2")

fun provideBookList() = listOf(provideBook1(), provideBook2())

fun provideBookResult() = BookResults(books = provideBookList())

fun provideBookResponse() = BookResponse(results = provideBookResult())