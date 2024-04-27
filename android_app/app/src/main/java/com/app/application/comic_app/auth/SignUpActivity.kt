package com.app.application.comic_app.auth

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.application.comic_app.R
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.databinding.ActivitySignUpBinding
import com.app.application.comic_app.model.MessageRegAccount
import com.app.application.comic_app.model.ModelPostRegAccount
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        onClick()
    }

    private fun init() {
        binding.apply {
            progressDialog = ProgressDialog(this@SignUpActivity).apply {
                this.setCancelable(false)
            }
        }
    }

    private fun onClick() {
        binding.apply {
            iconBAck.setOnClickListener { finish() }

            buttonLogin.setOnClickListener {
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString().trim()
                val name = inputName.text.toString().trim()
                val phoneNumber = inputPhoneNumber.text.toString().trim()

                if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(this@SignUpActivity, "Khong duoc de trong!", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val model = ModelPostRegAccount(
                    password = password,
                    ten = name,
                    email = email,
                    sdt = phoneNumber
                )
                progressDialog.show()
                RetrofitInstance.apiService.createNewAccount(model)
                    .enqueue(object: Callback<MessageRegAccount> {
                        override fun onResponse(
                            call: Call<MessageRegAccount>,
                            response: Response<MessageRegAccount>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(this@SignUpActivity, response.body()?.message ?: "", Toast.LENGTH_LONG).show()
                                inputEmail.text.clear()
                                inputName.text.clear()
                                inputPhoneNumber.text.clear()
                                inputPassword.text.clear()
                            }

                            progressDialog.cancel()
                        }

                        override fun onFailure(call: Call<MessageRegAccount>, t: Throwable) {
                            progressDialog.cancel()
                            Toast.makeText(this@SignUpActivity, "Da co loi xay ra", Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }
    }
}