package com.bookshelf.app.registration.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.favre.lib.crypto.bcrypt.BCrypt
import com.bookshelf.app.core.network.NetworkResponse
import com.bookshelf.app.core.network.exceptionhandlers.ApiFailureException
import com.bookshelf.app.registration.data.models.IPAddressResponse
import com.bookshelf.app.registration.data.models.SignupResult
import com.bookshelf.app.registration.data.tables.CountryEntity
import com.bookshelf.app.registration.data.tables.UserCredsEntity
import com.bookshelf.app.registration.domain.usecase.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * ViewModel class responsible for handling the user signup process and managing related data.
 *
 * @param useCase The use case providing registration-related operations and business logic.
 */
@HiltViewModel
class SignupViewModel @Inject constructor(val useCase: RegistrationUseCase) : ViewModel() {
    // Channels for emitting various states and results
    private val _isLoading = Channel<Boolean>()
    val isLoading = _isLoading.receiveAsFlow()

    private val _apiError = Channel<ApiFailureException?>()
    val apiError = _apiError.receiveAsFlow()

    private val _signupResult = Channel<SignupResult>()
    val signupResult = _signupResult.receiveAsFlow()

    private val _signUpButtonState = Channel<Boolean>()
    val signUpButtonState = _signUpButtonState.receiveAsFlow()

    private val _countryList = Channel<MutableList<CountryEntity>>()
    val countryList = _countryList.receiveAsFlow()

    private val _ipInfo = Channel<IPAddressResponse>()
    val ipInfo = _ipInfo.receiveAsFlow()

    /**
     * Initialize the view model and retrieve country data if not available in the database.
     */
    init {
        viewModelScope.launch {
            if (useCase.getCountryRowCount() == 0) {
                getCountryList()
            } else {
                getCountryListFromDb()
            }
        }
    }

    private suspend fun getCountryList() {
        useCase.getCountryList().collect {
            withContext(Dispatchers.Main.immediate) {
                when (it) {
                    is NetworkResponse.Error -> {
                        _apiError.send(it.error)
                    }

                    is NetworkResponse.Loading -> {
                        _isLoading.send(it.isLoading)
                    }

                    is NetworkResponse.Success -> {
                        val countriesList = it.data.data.mapNotNull { (_, value) ->
                            value.country?.let { countryName ->
                                CountryEntity(country = countryName)
                            }
                        }.toMutableList()
                        _countryList.send(countriesList)

                        val countriesToInsert = it.data.data.values.map {
                            it.country?.let { it1 -> CountryEntity(country = it1) }
                        }
                        countriesToInsert.forEach {
                            if (it != null) {
                                insertCountriesIntoDb(it)
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Get the user's IP address information.
     */
    fun getIPAddressInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getIpInfo().collect {
                withContext(Dispatchers.Main.immediate) {
                    when (it) {
                        is NetworkResponse.Error -> _apiError.send(it.error)
                        is NetworkResponse.Loading -> _isLoading.send(it.isLoading)
                        is NetworkResponse.Success -> {
                            _ipInfo.send(it.data)
                        }
                    }
                }
            }
        }
    }

    private fun insertCountriesIntoDb(countryEntity: CountryEntity) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            useCase.insertCountriesInDb(countryEntity)
        }
    }

    private fun getCountryListFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val countryEntity = useCase.getCountriesFromDb()
            withContext(Dispatchers.Main.immediate) {
                countryEntity?.let {
                    _countryList.send(it)
                } ?: _apiError.send(ApiFailureException("Something went wrong", code = 500))
            }
        }
    }

    /**
     * Attempt to sign up a new user with the provided information.
     *
     * @param username The user's chosen username.
     * @param email The user's email for registration.
     * @param password The chosen password for registration.
     * @param country The selected user's country.
     */
    fun signup(username: String, email: String, password: String, country: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingUser = useCase.getUserByUserEmail(email)
            withContext(Dispatchers.Main.immediate) {
                when {
                    (existingUser != null) -> {
                        _signupResult.send(SignupResult.UserEmailTaken)
                    }

                    else -> {
                        val hashedPassword = hashPassword(password)
                        try {
                            useCase.insertUserCreds(
                                UserCredsEntity(
                                    userName = username,
                                    email = email,
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

    /**
     * Update the state of the sign-up button.
     *
     * @param state The new state of the sign-up button.
     */
    fun updateSignUpButtonState(state: Boolean) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _signUpButtonState.send(state)
        }
    }
}
