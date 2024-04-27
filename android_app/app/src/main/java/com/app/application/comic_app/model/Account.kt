package com.app.application.comic_app.model

import com.google.gson.Gson

data class Account(
    var favourite: MutableList<Any>,
    var address: MutableList<Any>,
//    var wishlist: MutableList<Any>,
//    var isBlocked: Boolean = false,
    var _id: String = "",
//    var account: String = "",
    var password: String = "",
//    var tokenUser: String = "",
    var ten: String = "",
    var email: String = "",
    var sdt: String = "",
    var favorites: MutableList<String> = mutableListOf(),
    var readingHistory: MutableList<String> = mutableListOf()
) {
    companion object {
        fun convertAccountToString(account: Account): String {
            val gson = Gson()
            val jsonString = gson.toJson(account)
            return jsonString
        }

        fun convertStringToAccount(string: String): Account {
            val gson = Gson()
            val account: Account = gson.fromJson(string, Account::class.java)
            return account
        }
    }
}

data class AccountResponse(
    var success: Boolean = true,
    var data: MutableList<Account> = mutableListOf(),
    var message: String = ""
) {}