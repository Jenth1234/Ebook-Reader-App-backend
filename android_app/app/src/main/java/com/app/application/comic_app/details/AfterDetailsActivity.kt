package com.app.application.comic_app.details

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.database.local.SharePrefUtils
import com.app.application.comic_app.databinding.ActivityAfterDetailsBinding
import com.app.application.comic_app.model.Account
import com.app.application.comic_app.model.Book
import com.app.application.comic_app.model.ResponseFavorite
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AfterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAfterDetailsBinding
    private lateinit var progressDialog: ProgressDialog
    private val viewModel: AfterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAfterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        val bookJson = intent.getStringExtra("bookJson") ?: "{}"
        val book = Book.convertToBook(bookJson)
        binding.apply {
            Glide.with(this@AfterDetailsActivity).load(book.image).into(imageBook)
            textAuthorName.text = book.author
            textBookName.text = book.title
            textDes.text = book.description
        }

        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
        }

        viewModel.apply {
            isLoading.observe(this@AfterDetailsActivity) {
                if (it) progressDialog.show() else progressDialog.cancel()
            }
        }
    }

    private fun onClick() {
        binding.buttonReadingBook.setOnClickListener {
            val bookJson = intent.getStringExtra("bookJson") ?: "{}"
            val book = Book.convertToBook(bookJson)
            val idUserCurrent = SharePrefUtils.idUser

            progressDialog.show()
            RetrofitInstance.apiService
                .handleSaveHistory(idUserCurrent, book._id)
                .enqueue(object: Callback<ResponseFavorite> {
                    override fun onResponse(
                        call: Call<ResponseFavorite>,
                        response: Response<ResponseFavorite>
                    ) {
                        progressDialog.cancel()

                        // save to local
                        val accountJson = SharePrefUtils.accountJson
                        val accountCurrent = Account.convertStringToAccount(accountJson)
                        accountCurrent.readingHistory.add(book._id)
                        val accountStr = Account.convertAccountToString(accountCurrent)
                        SharePrefUtils.accountJson = accountStr
                    }

                    override fun onFailure(call: Call<ResponseFavorite>, t: Throwable) {
                        progressDialog.cancel()
                    }
                })

            val intent = Intent(this@AfterDetailsActivity, BookDetailsActivity::class.java)
            intent.putExtra("url_pdf", book.file)
            intent.putExtra("id", book._id)
            intent.putExtra("title", book.title)
            startActivity(intent)

            progressDialog.cancel()
        }

        binding.iconBack.setOnClickListener { finish() }
    }
}