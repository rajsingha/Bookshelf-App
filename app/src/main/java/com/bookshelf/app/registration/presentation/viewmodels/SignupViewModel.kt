package com.bookshelf.app.registration.presentation.viewmodels

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.bookshelf.app.registration.data.models.SignupResult
import com.bookshelf.app.registration.data.tables.UserCredsEntity
import com.bookshelf.app.registration.domain.usecase.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(val useCase: RegistrationUseCase) : ViewModel() {
    private val _signupResult = Channel<SignupResult>()
    val signupResult = _signupResult.receiveAsFlow()

    fun signup(username: String, password: String, country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = useCase.getUserByUsername(username)
            withContext(Dispatchers.Main.immediate) {
                when {
                    (existingUser != null) -> {
                        _signupResult.send(SignupResult.UsernameTaken)
                    }

                    else -> {
                        val hashedPassword = hashPassword(password)
                        try {
                            useCase.insertUserCreds(
                                UserCredsEntity(
                                    userName = username,
                                    passwordHash = hashedPassword,
                                    country = country
                                )
                            )
                            _signupResult.send(SignupResult.Success)
                        } catch (e: Exception) {
                            _signupResult.send(SignupResult.Failure)
                        }
                    }
                }
            }
        }
    }

    private fun hashPassword(enteredPass: String, cost: Int = 12): String =
        BCrypt.withDefaults().hashToString(cost, enteredPass.toCharArray())

    fun getIpAddress(): String? {
        val connectivityManager =
            useCase.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network: Network? = connectivityManager.activeNetwork
        val networkCapabilities: NetworkCapabilities? =
            connectivityManager.getNetworkCapabilities(network)

        if (networkCapabilities != null) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            ) {

                val linkProperties = connectivityManager.getLinkProperties(network)
                linkProperties?.linkAddresses?.forEach {
                    val ip = it.address.hostAddress
                    if (ip.contains(":").not()) {
                        return ip
                    }
                }
            }
        }

        return null
    }
}
