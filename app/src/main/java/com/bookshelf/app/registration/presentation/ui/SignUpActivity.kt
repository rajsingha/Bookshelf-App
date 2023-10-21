package com.bookshelf.app.registration.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.core.utils.collectLatestLifecycleFlow
import com.bookshelf.app.core.utils.showToast
import com.bookshelf.app.core.utils.validateEmail
import com.bookshelf.app.core.utils.validatePassword
import com.bookshelf.app.databinding.ActivitySignUpBinding
import com.bookshelf.app.registration.data.models.SignupResult
import com.bookshelf.app.registration.presentation.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val signupViewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiObservers()
    }

    private fun apiObservers() {
        collectLatestLifecycleFlow(signupViewModel.signupResult) {
            when (it) {
                SignupResult.Failure -> {
                    showToast("Signup Failed")
                }

                SignupResult.Success -> {
                    showToast("Signup Success")
                }

                SignupResult.UserEmailTaken -> {
                    showToast("Email taken")
                }
            }
        }

        collectLatestLifecycleFlow(signupViewModel.signUpButtonState) {
            binding.btnSignUp.isEnabled = it
        }
    }

    override fun onResume() {
        super.onResume()
        setOnClickListener()
    }

    private fun setOnClickListener() {
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
                country = "India"
            )
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

            (binding.etPassword.text?.isEmpty() == true || binding.tilPassword.isErrorEnabled) -> {
                signupViewModel.updateSignUpButtonState(false)
            }

            else -> signupViewModel.updateSignUpButtonState(true)
        }
    }
}