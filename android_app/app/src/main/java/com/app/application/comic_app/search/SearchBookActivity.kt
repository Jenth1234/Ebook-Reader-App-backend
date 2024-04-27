package com.app.application.comic_app.search

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.recyclerview.widget.GridLayoutManager
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.databinding.ActivitySearchBookBinding
import com.app.application.comic_app.details.AfterDetailsActivity
import com.app.application.comic_app.fragment.adapter.BookAdapter
import com.app.application.comic_app.model.Book
import com.app.application.comic_app.model.BookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SearchBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBookBinding
    private lateinit var listBook: MutableList<Book>
    private lateinit var progressDialog: ProgressDialog
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
        getDb()
    }

    private fun init() {
        listBook = mutableListOf()
        progressDialog = ProgressDialog(this).apply {
            setCancelable(false)
        }

        bookAdapter = BookAdapter().apply {
            setNewInstance(listBook)
            setOnItemClickListener { _, _, position ->
                val `object` = data[position]
                val intent = Intent(this@SearchBookActivity, AfterDetailsActivity::class.java)
                intent.apply {
                    val bookJson = Book.convertToString(`object`)
                    putExtra("bookJson", bookJson)
                }
                startActivity(intent)
            }
        }

        binding.rcv.apply {
            layoutManager = GridLayoutManager(this@SearchBookActivity, 2)
            adapter = bookAdapter
        }
    }

    private fun onClick() {
        binding.apply {
            iconBack.setOnClickListener {finish()}

            icon.setOnClickListener {
                val keySearch = inputSearch.text.toString().trim()
                if (keySearch.isEmpty()) {
                    bookAdapter.apply {
                        setNewInstance(listBook)
                        notifyDataSetChanged()
                    }
                } else {
                    val listFilter = mutableListOf<Book>()
                    for (item in listBook) {
                        if (item.title.toLowerCase(Locale.ROOT).contains(keySearch.toLowerCase())) {
                            listFilter.add(item)
                        }
                    }
                    bookAdapter.apply {
                        setNewInstance(listFilter)
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

    private fun getDb() {
        RetrofitInstance.apiService.getAllBook()
            .enqueue(object: Callback<BookResponse> {
                override fun onResponse(
                    call: Call<BookResponse>,
                    response: Response<BookResponse>
                ) {
                    progressDialog.cancel()
                    if (response.isSuccessful) {
                        listBook.apply {
                            clear()
                            addAll(response.body()!!.data)
                        }
                        bookAdapter.apply {
                            setNewInstance(listBook)
                            notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                    progressDialog.cancel()
                }
            })
    }
}