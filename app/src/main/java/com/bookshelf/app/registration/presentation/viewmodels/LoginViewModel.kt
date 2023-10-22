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
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(val useCase: RegistrationUseCase) : ViewModel() {
    private val _loginResult = Channel<LoginResult>()
    val loginResult = _loginResult.receiveAsFlow()

    private val _signInButtonState = Channel<Boolean>()
    val signInButtonState = _signInButtonState.receiveAsFlow()


    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userCredsEntity = useCase.getUserByUserEmail(email)
            withContext(Dispatchers.Main.immediate) {
                when {
                    (userCredsEntity != null && isPasswordVerified(
                        password,
                        userCredsEntity.passwordHash
                    )) -> {
                        _loginResult.send(LoginResult.Success(userCredsEntity))
                    }

                    else -> _loginResult.send(LoginResult.Failure)
                }
            }
        }
    }


    private fun isPasswordVerified(enteredPassword: String, storedHash: String): Boolean {
        return BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHash).verified
    }

    fun updateSignInButtonState(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _signInButtonState.send(state)
        }
    }
}
