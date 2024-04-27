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
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.databinding.FragmentHomeBinding
import com.app.application.comic_app.details.AfterDetailsActivity
import com.app.application.comic_app.details.AllCategoryBookActivity
import com.app.application.comic_app.details.BookDetailsActivity
import com.app.application.comic_app.fragment.adapter.BookAdapter
import com.app.application.comic_app.fragment.adapter.SlideAdapter
import com.app.application.comic_app.model.Book
import com.app.application.comic_app.model.BookResponse
import com.app.application.comic_app.search.SearchBookActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var bookAdapter1: BookAdapter
    private lateinit var bookAdapter2: BookAdapter
    private lateinit var bookAdapter3: BookAdapter
    private lateinit var bookAdapterOther: BookAdapter
    private lateinit var listBookCat1: MutableList<Book>
    private lateinit var listBookCat2: MutableList<Book>
    private lateinit var listBookCat3: MutableList<Book>
    private lateinit var listBookOther: MutableList<Book>
    private lateinit var listBookAll: MutableList<Book>
    private lateinit var progressDialog: ProgressDialog
    private lateinit var slideAdapter: SlideAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        init()
        onClick()
        getAllBooks()

        return binding.root
    }

    private fun init() {
        listBookAll = mutableListOf()
        listBookOther = mutableListOf()
        progressDialog = ProgressDialog(requireActivity()).apply {
            this.setCancelable(false)
        }
        bookAdapter1 = BookAdapter().apply {
            listBookCat1 = mutableListOf()
            setNewInstance(listBookCat1)
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
        bookAdapter2 = BookAdapter().apply {
            listBookCat2 = mutableListOf()
            setNewInstance(listBookCat2)
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
        bookAdapter3 = BookAdapter().apply {
            listBookCat3 = mutableListOf()
            setNewInstance(listBookCat3)
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

        bookAdapterOther = BookAdapter().apply {
            listBookOther = mutableListOf()
            setNewInstance(listBookOther)
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

        binding.apply {
            rcvBook1.adapter = bookAdapter1
            rcvBook2.adapter = bookAdapter2
            rcvBook3.adapter = bookAdapter3
            rcvBook4.adapter = bookAdapterOther
        }
    }

    private fun onClick() {
        binding.apply {
            textSeeAllEcommerce.setOnClickListener {
                val intent = Intent(requireActivity(), AllCategoryBookActivity::class.java)
                intent.putExtra("key", "kt")
                startActivity(intent)
            }

            textSeeAllHistory.setOnClickListener {
                val intent = Intent(requireActivity(), AllCategoryBookActivity::class.java)
                intent.putExtra("key", "ls")
                startActivity(intent)
            }

            textSeeAllVH.setOnClickListener {
                val intent = Intent(requireActivity(), AllCategoryBookActivity::class.java)
                intent.putExtra("key", "vh")
                startActivity(intent)
            }

            textSeeAllVH.setOnClickListener {
                val intent = Intent(requireActivity(), AllCategoryBookActivity::class.java)
                intent.putExtra("key", "kh")
                startActivity(intent)
            }

            layoutSearch.setOnClickListener {
                val intent = Intent(requireActivity(), SearchBookActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getAllBooks() {
        progressDialog.show()
        RetrofitInstance.apiService.getAllBook().enqueue(object: Callback<BookResponse>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<BookResponse>,
                response: Response<BookResponse>
            ) {
                if (response.isSuccessful) {
                    listBookAll.apply {
                        clear()
                        addAll(response.body()?.data ?: mutableListOf())
                    }

                    // sort list Book by vies
                    val listSorted = listBookAll.sortedByDescending { it.view }.take(5)
                    slideAdapter = SlideAdapter(requireActivity(), listSorted as MutableList<Book>)
                    binding.layoutSlide.adapter = slideAdapter

                    for (item in listBookAll) {
                        if (item.genre == "Kinh tế") {
                            listBookCat1.add(item)
                        } else if (item.genre == "Lịch Sử") {
                            listBookCat2.add(item)
                        } else if (item.genre == "Văn Học") {
                            listBookCat3.add(item)
                        } else {
                            listBookOther.add(item)
                        }
                    }

                    bookAdapter1.apply {
                        setNewInstance(listBookCat1.take(5) as MutableList<Book>)
                        notifyDataSetChanged()
                    }
                    bookAdapter2.apply {
                        setNewInstance(listBookCat2.take(5) as MutableList<Book>)
                        notifyDataSetChanged()
                    }
                    bookAdapter3.apply {
                        setNewInstance(listBookCat3.take(5) as MutableList<Book>)
                        notifyDataSetChanged()
                    }
                    bookAdapterOther.apply {
                        setNewInstance(listBookOther.take(5) as MutableList<Book>)
                        notifyDataSetChanged()
                    }
                    progressDialog.cancel()
                }
                progressDialog.cancel()
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                progressDialog.cancel()
                Toast.makeText(requireActivity(), "Đã có lỗi xảy ra!", Toast.LENGTH_LONG).show()
                Log.d("oke", t.message.toString())
            }
        })
    }
}