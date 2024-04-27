package com.app.application.comic_app.model

import com.google.gson.Gson

data class BookResponse(
    var success: Boolean = false,
    var data: MutableList<Book>
)

data class Book(
    var _id: String = "",
    var title: String = "",
    var author: String = "",
    var genre: String = "",
    var description: String = "",
    var image: String = "",
    var file: String = "",
    var view: Int = 0
) {

    companion object {
        fun convertToString(book: Book) = Gson().toJson(book)

        fun convertToBook(json: String) = Gson().fromJson(json, Book::class.java)
    }

}