package com.app.application.comic_app.model

data class MessageRegAccount(
    var success: Boolean = false,
    var message: String = "",
)

data class ModelPostRegAccount(
    var password: String = "",
    var ten: String = "",
    var email: String = "",
    var sdt: String = ""
)
