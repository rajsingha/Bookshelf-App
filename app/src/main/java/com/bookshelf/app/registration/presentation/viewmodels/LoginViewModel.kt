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

/**
 * ViewModel class responsible for handling the login process and managing related data.
 *
 * @param useCase The use case providing registration-related operations and business logic.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(val useCase: RegistrationUseCase) : ViewModel() {
    // Channels for emitting login results and sign-in button state
    private val _loginResult = Channel<LoginResult>()
    val loginResult = _loginResult.receiveAsFlow()

    private val _signInButtonState = Channel<Boolean>()
    val signInButtonState = _signInButtonState.receiveAsFlow()

    /**
     * Attempts to log in with the provided email and password.
     *
     * @param email The user's email for login.
     * @param password The entered password for login.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userCredsEntity = useCase.getUserByUserEmail(email)
            withContext(Dispatchers.Main.immediate) {
                when {
                    // Verifies the entered password with the stored hash
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

    /**
     * Verifies if the entered password matches the stored hash.
     *
     * @param enteredPassword The password entered by the user.
     * @param storedHash The stored password hash.
     * @return `true` if the entered password matches the stored hash; otherwise, `false`.
     */
    private fun isPasswordVerified(enteredPassword: String, storedHash: String): Boolean {
        return BCrypt.verifyer().verify(enteredPassword.toCharArray(), storedHash).verified
    }

    /**
     * Updates the state of the sign-in button.
     *
     * @param state The new state of the sign-in button.
     */
    fun updateSignInButtonState(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _signInButtonState.send(state)
        }
    }
}
