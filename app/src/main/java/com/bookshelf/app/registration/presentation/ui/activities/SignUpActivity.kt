package com.bookshelf.app.registration.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.bookshelf.app.R
import com.bookshelf.app.core.baseui.BaseActivity
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.core.utils.collectLatestLifecycleFlow
import com.bookshelf.app.core.utils.showToast
import com.bookshelf.app.core.utils.validateEmail
import com.bookshelf.app.core.utils.validatePassword
import com.bookshelf.app.databinding.ActivitySignUpBinding
import com.bookshelf.app.registration.data.models.IPAddressResponse
import com.bookshelf.app.registration.data.models.SignupResult
import com.bookshelf.app.registration.data.tables.CountryEntity
import com.bookshelf.app.registration.presentation.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val signupViewModel: SignupViewModel by viewModels()
    private var countryEntityList = mutableListOf<CountryEntity>()
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private var mutableCountryList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        apiObservers()
    }

    private fun initView() {
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableCountryList)
        binding.countrySpinner.adapter = arrayAdapter
    }

    private fun convertCountryToStringArray() {
        mutableCountryList.clear()
        mutableCountryList.add(getString(R.string.select_country))
        countryEntityList.forEach {
            mutableCountryList.add(it.country)
        }
        arrayAdapter.notifyDataSetChanged()
    }

    private fun apiObservers() {
        collectLatestLifecycleFlow(signupViewModel.countryList) {
            countryEntityList.clear()
            countryEntityList = it
            convertCountryToStringArray()
            signupViewModel.getIPAddressInfo()
        }

        collectLatestLifecycleFlow(signupViewModel.signupResult) {
            when (it) {
                SignupResult.Failure -> {
                    showToast("Signup Failed")
                }

                SignupResult.Success -> {
                    showToast("Signup Successful, you can Sign In now")
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                }

                SignupResult.UserEmailTaken -> {
                    showToast(getString(R.string.account_already_exist_please_do_sign_in))
                }
            }
        }

        collectLatestLifecycleFlow(signupViewModel.ipInfo) { ipAdressResponse ->
            setCountryIfAvailable(ipAdressResponse)
        }
        collectLatestLifecycleFlow(signupViewModel.apiError) {
            it?.message?.let { it1 -> showToast(it1) }
        }

        collectLatestLifecycleFlow(signupViewModel.isLoading) {
            showProgress(it)
        }

        collectLatestLifecycleFlow(signupViewModel.signUpButtonState) {
            binding.btnSignUp.isEnabled = it
        }
    }

    private fun setCountryIfAvailable(ipAddressResponse: IPAddressResponse) {
        ipAddressResponse.country?.let { ipCountry ->
            val isCountryInList = countryEntityList.any { it.country == ipCountry }
            if (isCountryInList) {
                val position = mutableCountryList.indexOf(ipCountry)
                binding.countrySpinner.setSelection(position)
            }
        }
    }

    override fun setOnclickListener() {
        binding.etUserName.doAfterTextChanged {
            if (isMinimum3chars(binding.etUserName.text.toString()).not()) {
                binding.tilUserName.error = getString(R.string.please_enter_minimum_3_characters)
                binding.tilUserName.isErrorEnabled = true
            } else {
                binding.tilUserName.isErrorEnabled = false
            }
            isAllFieldsValid()
        }

        binding.etUserEmail.doAfterTextChanged {
            if (validateEmail(binding.etUserEmail.text.toString()).not()) {
                binding.tilUserEmail.error = getString(R.string.please_enter_a_valid_email_address)
                binding.tilUserEmail.isErrorEnabled = true
            } else {
                binding.tilUserEmail.isErrorEnabled = false
            }
            isAllFieldsValid()
        }

        binding.etPassword.doAfterTextChanged {
            if (validatePassword(it.toString()).not()) {
                binding.tilPassword.error =
                    getString(R.string.password_must_contain_at_least_8_characters)
                binding.tilPassword.isErrorEnabled = true
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
            isAllFieldsValid()
        }

        binding.tvSignIn.clickWithDebounce {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        binding.btnSignUp.clickWithDebounce {
            signupViewModel.signup(
                binding.etUserName.text.toString(),
                binding.etUserEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.countrySpinner.selectedItem.toString()
            )
        }

        binding.countrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        isAllFieldsValid()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle nothing selected.
                }
            }
    }

    private fun isMinimum3chars(textField: String): Boolean {
        return textField.length >= 3
    }

    private fun isAllFieldsValid() {
        when {
            (binding.etUserName.text?.isEmpty() == true || binding.tilUserName.isErrorEnabled) -> {
                signupViewModel.updateSignUpButtonState(false)
            }

            (binding.etUserEmail.text?.isEmpty() == true || binding.tilUserEmail.isErrorEnabled) -> {
                signupViewModel.updateSignUpButtonState(false)
            }

            (binding.countrySpinner.selectedItem.toString() == mutableCountryList[0]
                    || binding.countrySpinner.selectedItem.toString().isEmpty()) -> {
                signupViewModel.updateSignUpButtonState(false)
            }

            (binding.etPassword.text?.isEmpty() == true || binding.tilPassword.isErrorEnabled) -> {
                signupViewModel.updateSignUpButtonState(false)
            }

            else -> signupViewModel.updateSignUpButtonState(true)
        }
    }
}
