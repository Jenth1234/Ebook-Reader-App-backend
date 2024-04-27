package com.app.application.comic_app.fragment

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.database.local.SharePrefUtils
import com.app.application.comic_app.databinding.FragmentHistoryBinding
import com.app.application.comic_app.details.AfterDetailsActivity
import com.app.application.comic_app.fragment.adapter.BookAdapter
import com.app.application.comic_app.fragment.adapter.BookFavoriteAdapter
import com.app.application.comic_app.model.Account
import com.app.application.comic_app.model.Book
import com.app.application.comic_app.model.BookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class   HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var listBook: MutableList<Book>
    private lateinit var progressBar: ProgressDialog
    private lateinit var bookAdapter: BookAdapter
    private lateinit var accountCurrent: Account

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHistoryBinding.inflate(layoutInflater)

        init()
        onClick()
        getDb()

        return binding.root
    }

    private fun init() {
        val accountJson = SharePrefUtils.accountJson
        accountCurrent = Account.convertStringToAccount(accountJson)

        listBook = mutableListOf()
        progressBar = ProgressDialog(requireActivity()).apply {
            this.setCancelable(false)
        }
        bookAdapter = BookAdapter().apply {
            setNewInstance(listBook)
            setOnItemClickListener { _, _, position ->
                val `object` = data[position]
                val intent = Intent(requireActivity(), AfterDetailsActivity::class.java)
                intent.apply {
                    val bookJson = Book.convertToString(`object`)
                    putExtra("bookJson", bookJson)
                }
                startActivity(intent)
            }
        }
        binding.rcv.adapter = bookAdapter
        binding.rcv.layoutManager = GridLayoutManager(requireActivity(), 2)
    }

    private fun onClick() {

    }

    private fun getDb() {
        progressBar.show()
        RetrofitInstance.apiService.getAllBook().enqueue(object : Callback<BookResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                if (response.isSuccessful) {
                    listBook.clear()
                    for (book in response.body()!!.data) {
                        for (idBook in accountCurrent.readingHistory) {
                            if (book._id == idBook) {
                                listBook.add(book)
                            }
                        }
                    }
                    bookAdapter.apply {
                        setNewInstance(listBook)
                        notifyDataSetChanged()
                    }
                    progressBar.cancel()
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                progressBar.cancel()
                Toast.makeText(requireActivity(), "Đã có lỗi xảy ra!", Toast.LENGTH_LONG).show()
                Log.d("ok", t.message.toString())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val accountJson = SharePrefUtils.accountJson
        accountCurrent = Account.convertStringToAccount(accountJson)
        getDb()
    }
}