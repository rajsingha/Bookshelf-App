package com.bookshelf.app.core.utils

import android.content.Context
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import java.util.regex.Pattern

const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&()])(?=\\S+\$).{8,}\$"
const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

/**
 * Displays a toast message in the context of the provided `Context`.
 *
 * @param message The message to be displayed in the toast.
 * @param duration The duration for which the toast should be shown (default is `Toast.LENGTH_SHORT`).
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}


/**
 * Validates a password based on a specified pattern.
 *
 * @param password The password to be validated.
 * @return `true` if the password matches the specified pattern, `false` otherwise.
 */
fun validatePassword(password: String): Boolean {
    return Pattern.matches(PASSWORD_PATTERN, password)
}


/**
 * Validates an email address based on a specified pattern.
 *
 * @param email The email address to be validated.
 * @return `true` if the email address matches the specified pattern, `false` otherwise.
 */
fun validateEmail(email: String): Boolean {
    val pattern = Pattern.compile(EMAIL_PATTERN)
    val matcher = pattern.matcher(email)
    return matcher.matches()
}


/**
 * Attaches a double-click listener to a View with a specified double-click duration.
 *
 * @param doubleClickDuration The time interval in milliseconds within which two clicks are considered a double-click.
 * @param onDoubleClick The lambda function to execute when a double-click is detected.
 */
fun View.setDoubleClickListener(doubleClickDuration: Long = 500, onDoubleClick: () -> Unit) {
    var lastClickTime: Long = 0
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < doubleClickDuration) {
            // Double-click detected
            onDoubleClick()
        }
        lastClickTime = currentTime
    }
}


/**
 * Attaches a double-click listener to a MenuItem with a specified double-click duration.
 *
 * @param doubleClickDuration The time interval in milliseconds within which two clicks are considered a double-click.
 * @param onDoubleClick The lambda function to execute when a double-click is detected.
 */
fun MenuItem.setDoubleClickListener(doubleClickDuration: Long = 500, onDoubleClick: () -> Unit) {
    var lastClickTime: Long = 0
    setOnMenuItemClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < doubleClickDuration) {
            onDoubleClick()
        }
        lastClickTime = currentTime
        false // Return false to allow normal item selection
    }
}
