package com.bookshelf.app.core.utils

import android.content.Context
import android.widget.Toast
import java.util.regex.Pattern

const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&()])(?=\\S+\$).{8,}\$"
const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

fun validatePassword(password: String): Boolean {
    return Pattern.matches(PASSWORD_PATTERN, password)
}

fun validateEmail(email: String): Boolean {
    val pattern = Pattern.compile(EMAIL_PATTERN)
    val matcher = pattern.matcher(email)
    return matcher.matches()
}