package com.app.application.comic_app.details

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.database.local.SharePrefUtils
import com.app.application.comic_app.databinding.ActivityBookDetailsBinding
import com.app.application.comic_app.model.Account
import com.app.application.comic_app.model.ResponseFavorite
import com.github.barteksc.pdfviewer.PDFView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookDetailsBinding
    private lateinit var progressDialog: ProgressDialog
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        onClick()

        val accountCurrentStr = SharePrefUtils.accountJson
        val accountCurrent = Account.convertStringToAccount(accountCurrentStr)
        val idBookIntent = intent.getStringExtra("id") ?: ""
        for (idBook in accountCurrent.favorites) {
            if (idBook == idBookIntent) {
                binding.icon.setImageResource(R.drawable.icon_heart)
                isFavorite = true
                break
            }
        }
    }

    private fun init() {
        var pdfUrlDefault = "https://unec.edu.az/application/uploads/2014/12/pdf-sample.pdf"
        val pdfUrl = intent.getStringExtra("url_pdf") ?: pdfUrlDefault
        val title = intent.getStringExtra("title") ?: "Book Name"
        RetrievePDFFromURL(binding.pdfView).execute(pdfUrl)
        binding.textBookName.text = title

        progressDialog = ProgressDialog(this@BookDetailsActivity).apply {
            setCancelable(false)
        }

        progressDialog.show()
        RetrofitInstance.apiService
            .handleRaise1View(intent.getStringExtra("id") ?: "")
            .enqueue(object: Callback<Int> {
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    progressDialog.cancel()
                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    progressDialog.cancel()
                }
            })
    }

    private fun onClick() {
        binding.apply {
            iconBAck.setOnClickListener { finish() }
            icon.setOnClickListener {
                if (isFavorite) {
                    Toast.makeText(this@BookDetailsActivity, "Sach da co trong muc yeu thic!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                handleFavorite()
            }
        }
    }

    private fun handleFavorite() {
        progressDialog = ProgressDialog(this@BookDetailsActivity).apply {
            this.setCancelable(false)
        }
        progressDialog.show()
        val idUser = SharePrefUtils.idUser
        val idBook = intent.getStringExtra("id") ?: ""
        RetrofitInstance.apiService
            .handleFavoriteBook(idUser = idUser, idBook = idBook)
            .enqueue(object: Callback<ResponseFavorite> {
                override fun onResponse(
                    call: Call<ResponseFavorite>,
                    response: Response<ResponseFavorite>
                ) {
                    if (response.isSuccessful) {
                        binding.icon.setImageResource(R.drawable.icon_heart)
                        val accountCurrentStr = SharePrefUtils.accountJson
                        val accountCurrent = Account.convertStringToAccount(accountCurrentStr)
                        accountCurrent.favorites.add(idBook)
                        val accountCurrentJson = Account.convertAccountToString(accountCurrent)
                        SharePrefUtils.accountJson = accountCurrentJson
                    }
                    progressDialog.cancel()
                }

                override fun onFailure(call: Call<ResponseFavorite>, t: Throwable) {
                    progressDialog.cancel()
                    Toast.makeText(this@BookDetailsActivity, "Da co loi xay ra!", Toast.LENGTH_LONG).show()
                }
            })

    }

    @SuppressLint("StaticFieldLeak")
    inner class RetrievePDFFromURL(pdfView: PDFView) :
        AsyncTask<String, Void, InputStream>() {

       //tạo biến cho pdf
        val mypdfView: PDFView = pdfView

           //thực hiện việc tải file PDF từ địa chỉ URL được truyền vào.
        override fun doInBackground(vararg params: String?): InputStream? {
           //tạo luồng đầu vào
            var inputStream: InputStream? = null
            try {

                val url = URL(params.get(0))

//                Nếu kết nối thành công và nhận được mã phản hồi là 200, nó sẽ trả
//                về một InputStream chứa dữ liệu của file PDF.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection


                if (urlConnection.responseCode == 200) {

                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            }

            catch (e: Exception) {

                e.printStackTrace()
                return null;
            }
            // gọi inputStream để chứa dữ liệu
            return inputStream;
        }

        //
        @Deprecated("Deprecated in Java", ReplaceWith("mypdfView.fromStream(result).load()"))
        override fun onPostExecute(result: InputStream?) {
            // on below line we are loading url within our
            // pdf view on below line using input stream.
            mypdfView.fromStream(result).load()

        }
    }
}