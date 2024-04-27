package com.app.application.comic_app.auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.application.comic_app.MainActivity
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.database.local.SharePrefUtils
import com.app.application.comic_app.databinding.ActivityLoginBinding
import com.app.application.comic_app.model.Account
import com.app.application.comic_app.model.AccountResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class   LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var listAccount: MutableList<Account>
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        onClick()
    }
        //truy vấn dữ liệu
    private fun init() {
        progressDialog = ProgressDialog(this).apply {
            this.setCancelable(false)
        }
        listAccount = mutableListOf()

        RetrofitInstance.apiService.getAllAccount()
            .enqueue(object: Callback<AccountResponse> {
                override fun onResponse(
                    call: Call<AccountResponse>,
                    response: Response<AccountResponse>
                ) {
                    if (response.isSuccessful) {
                        listAccount.apply {
                            clear()
                            addAll(response.body()?.data ?: mutableListOf())
                        }
                    }
                    progressDialog.cancel()
                }

                override fun onFailure(call: Call<AccountResponse>, t: Throwable) {
                    progressDialog.cancel()
                    Log.d("ok", t.message.toString())
                    Toast.makeText(this@LoginActivity, "Da co loi xay ra", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun onClick() {
        binding.apply {
            buttonLogin.setOnClickListener {

                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString().trim()
                var isFound = false

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@LoginActivity, "Khong duoc de trong!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                for (account in listAccount) {
                    if (account.email == email && account.password == password) {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                        SharePrefUtils.idUser = account._id
                        val accountJson = Account.convertAccountToString(account)
                        SharePrefUtils.accountJson = accountJson
                        isFound = true
                        break
                    }
                }

                if (!isFound) {
                    Toast.makeText(this@LoginActivity, "Khong tim thay tai khoan phu hop!", Toast.LENGTH_LONG).show()
                }

                progressDialog.cancel()
            }

            textSignUp.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }
}