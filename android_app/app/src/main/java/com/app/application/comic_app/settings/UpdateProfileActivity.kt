package com.app.application.comic_app.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.compose.ui.state.ToggleableState
import com.app.application.comic_app.R
import com.app.application.comic_app.auth.LoginActivity
import com.app.application.comic_app.database.api.RetrofitInstance
import com.app.application.comic_app.database.local.SharePrefUtils
import com.app.application.comic_app.databinding.ActivityUpdateProfileBinding
import com.app.application.comic_app.model.Account
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.apply {
            iconBack.setOnClickListener { finish() }

            buttonUpdate.setOnClickListener {
                val email = inputEmail.text.toString().trim()
                val password = inputPassword.text.toString().trim()

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(this@UpdateProfileActivity, "Khong duoc de trong!", Toast
                        .LENGTH_LONG).show()
                    return@setOnClickListener
                }

                val accountJson = SharePrefUtils.accountJson
                val accountCurrent = Account.convertStringToAccount(accountJson)
                accountCurrent.email = email
                accountCurrent.password = password
                RetrofitInstance.apiService.handleUpdateAccount(
                    id = accountCurrent._id, account = accountCurrent
                ).enqueue(object: Callback<ResponseUpdate> {
                    override fun onResponse(
                        call: Call<ResponseUpdate>,
                        response: Response<ResponseUpdate>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@UpdateProfileActivity, "Cap nhat thanh cong", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@UpdateProfileActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                    }

                    override fun onFailure(call: Call<ResponseUpdate>, t: Throwable) {
                        Toast.makeText(this@UpdateProfileActivity, "Da co loi xay ra!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    data class ResponseUpdate(
        var success: Boolean = false,
        var message: String = "",
    )
}