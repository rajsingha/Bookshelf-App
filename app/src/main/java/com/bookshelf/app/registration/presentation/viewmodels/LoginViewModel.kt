package com.bookshelf.app.registration.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.bookshelf.app.registration.data.models.LoginResult
import com.bookshelf.app.registration.domain.usecase.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.regex.Pattern
import javax.inject.Inject

const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%&()])(?=\\S+\$).{8,}\$"

@HiltViewModel
class LoginViewModel @Inject constructor(private val useCase: RegistrationUseCase) : ViewModel() {
    private val _loginResult = Channel<LoginResult>()
    val loginResult = _loginResult.receiveAsFlow()

    private val _signInButtonState = Channel<Boolean>()
    val signInButtonState = _signInButtonState.receiveAsFlow()

    fun login(userName: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userCreds = useCase.getUserByUsername(userName)
            withContext(Dispatchers.Main.immediate) {
                when {
                    (userCreds != null && isPasswordVerified(password, userCreds.passwordHash)) -> {
                        _loginResult.send(LoginResult.Success(userCreds))
                    }

                    else -> _loginResult.send(LoginResult.Failure)
                }
            }
        }
    }

    private fun isPasswordVerified(enteredPassword: String, storedHash: String): Boolean {
        return BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHash).verified
    }

    fun validatePassword(password: String): Boolean {
        return Pattern.matches(PASSWORD_PATTERN, password)
    }

    fun updateSignInButtonState(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _signInButtonState.send(state)
        }
    }
}
