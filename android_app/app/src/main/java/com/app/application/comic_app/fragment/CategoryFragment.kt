//package com.app.application.comic_app.fragment
//
//import android.annotation.SuppressLint
//import android.app.ProgressDialog
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ProgressBar
//import android.widget.Toast
//import com.app.applicatin.comic_app.R
//import com.app.application.comic_app.database.api.RetrofitInstance
//import com.app.application.comic_app.databinding.FragmentCategoryBinding
//import com.app.application.comic_app.databinding.FragmentFavoriteBinding
//import com.app.application.comic_app.details.BookDetailsActivity
//import com.app.application.comic_app.fragment.adapter.BookFavoriteAdapter
//import com.app.application.comic_app.fragment.adapter.SlideAdapter
//import com.app.application.comic_app.model.Book
//import com.app.application.comic_app.model.BookResponse
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//class CategoryFragment : Fragment() {
//
//    private lateinit var binding: FragmentFavoriteBinding
//    private lateinit var listBook: MutableList<Book>
//    private lateinit var progressBar: ProgressDialog
//    private lateinit var bookAdapter: BookFavoriteAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        binding = FragmentCategoryBinding.inflate(layoutInflater)
//
//        init()
//        onClick()
//        getDb()
//
//        return binding.root
//    }
//
//    private fun init() {
//        listBook = mutableListOf()
//        progressBar = ProgressDialog(requireActivity()).apply {
//            this.setCancelable(false)
//        }
//        bookAdapter = BookFavoriteAdapter().apply {
//            setNewInstance(listBook)
//            setOnItemChildClickListener { _, view, position ->
//                when (view.id) {
//                    R.id.textCategory1 -> {}
//                }
//            }
//            setOnItemClickListener { _, _, position ->
//                val `object` = data[position]
//                val intent = Intent(requireActivity(), BookDetailsActivity::class.java)
//                intent.apply {
//                    putExtra("url_pdf", `object`.file)
//                    putExtra("title", `object`.title)
//                }
//                startActivity(intent)
//            }
//        }
//        binding.rcv.adapter = bookAdapter
//    }
//
//    private fun onClick() {
//
//    }
//
//    private fun getDb() {
//        progressBar.show()
//        RetrofitInstance.apiService.getAllBook().enqueue(object : Callback<BookResponse> {
//            @SuppressLint("NotifyDataSetChanged")
//            override fun onResponse(
//                call: Call<BookResponse>,
//                response: Response<BookResponse>
//            ) {
//                if (response.isSuccessful) {
//                    listBook.apply {
//                        clear()
//                        addAll(response.body()?.data ?: mutableListOf())
//                        shuffle()
//                        take(7)
//                    }
//                    bookAdapter.apply {
//                        setNewInstance(listBook)
//                        notifyDataSetChanged()
//                    }
//                    progressBar.cancel()
//                }
//            }
//
//            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
//                progressBar.cancel()
//                Toast.makeText(requireActivity(), "Đã có lỗi xảy ra!", Toast.LENGTH_LONG).show()
//                Log.d("duylt", t.message.toString())
//            }
//        })
//    }
//}