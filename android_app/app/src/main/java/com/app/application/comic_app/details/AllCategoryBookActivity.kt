package com.app.application.comic_app.details

import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.databinding.ActivityAllCategoryBookBinding
import com.app.application.comic_app.fragment.adapter.BookAdapter
import com.app.application.comic_app.model.Book
import com.app.application.comic_app.model.BookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllCategoryBookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllCategoryBookBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var listBook: MutableList<Book>
    private lateinit var bookAdapter: BookAdapter
    private var flagKey: String = "kt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCategoryBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
        getDatabase()
    }

    private fun init() {
        flagKey = intent.getStringExtra("key") ?: "kt"

        binding.textCategory.text =
            when (flagKey) {
                "kt" -> "Kinh Te"
                "ls" -> "Lich su"
                "vh" -> "Van hoc"
                else -> "Khac"
            }

        listBook = mutableListOf()
        bookAdapter = BookAdapter().apply {
            setNewInstance(mutableListOf())//thay thế danh sách rong truyen dl vào
            setOnItemClickListener { _, _, position ->

            }
        }
        progressDialog = ProgressDialog(this).apply {
            this.setCancelable(false)
        }
        binding.rcv.apply {
            layoutManager = GridLayoutManager(this@AllCategoryBookActivity, 2)
            adapter = bookAdapter
        }
    }

    private fun onClick() {
        binding.iconBack.setOnClickListener { finish() }
    }

    private fun getDatabase() {
        progressDialog.show()
        RetrofitInstance.apiService.getAllBook().enqueue(object: Callback<BookResponse> {
//            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    val listBookTemp =
                        when (flagKey) {
                            "kt" -> response.body()!!.data.filter { it.genre == "Kinh tế" } as MutableList
                            "ls" -> response.body()!!.data.filter { it.genre == "Lịch Sử" } as MutableList<Book>
                            "vh" -> response.body()!!.data.filter { it.genre == "Văn Học" } as MutableList<Book>
                            else -> response.body()!!.data.shuffle() as MutableList<Book>
                        }
                    bookAdapter.apply {
                        setNewInstance(listBookTemp)
                        notifyDataSetChanged()
                    }
                }
                progressDialog.cancel()
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                progressDialog.cancel()
            }
        })
    }
}