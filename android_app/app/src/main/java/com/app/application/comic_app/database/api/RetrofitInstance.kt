package com.app.application.comic_app.database.api

import com.app.application.comic_app.model.Account
import com.app.application.comic_app.model.AccountResponse
import com.app.application.comic_app.model.Book
import com.app.application.comic_app.model.BookResponse
import com.app.application.comic_app.model.MessageRegAccount
import com.app.application.comic_app.model.ModelPostRegAccount
import com.app.application.comic_app.model.ResponseFavorite
import com.app.application.comic_app.settings.UpdateProfileActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private val gson: Gson = GsonBuilder().setDateFormat("dd/MM/yyyy").create()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val host: String = "172.20.10.6"
    private val port: String = "8080"
    private val url: String = "http://$host:$port/"

    val apiService: ApiService = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()
        .create(ApiService::class.java)


    interface ApiService {
        @GET("books")
        fun getAllBook(): Call<BookResponse>

        @GET("accounts")
        fun getAllAccount(): Call<AccountResponse>

        @POST("accounts")
        fun createNewAccount(@Body b: ModelPostRegAccount): Call<MessageRegAccount>

        @PUT("/accounts/{id}/add-favorite/{bookId}")
        fun handleFavoriteBook(
            @Path("id") idUser: String,
            @Path("bookId") idBook: String
        ): Call<ResponseFavorite>

        @PUT("/accounts/{id}/add-to-history/{bookId}")
        fun handleSaveHistory(
            @Path("id") idUser: String,
            @Path("bookId") idBook: String
        ): Call<ResponseFavorite>

        @PUT("/accounts/{id}")
        fun handleUpdateAccount(
            @Path("id") id: String,
            @Body account: Account
        ): Call<UpdateProfileActivity.ResponseUpdate>

        @PUT("/books/views/raise/{id}")
        fun handleRaise1View(
            @Path("id") id: String
        ): Call<Int>
    }
}