package com.bookshelf.app.registration.presentation.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.bookshelf.app.R
import com.bookshelf.app.core.utils.collectLatestLifecycleFlow
import com.bookshelf.app.databinding.ActivitySignInBinding
import com.bookshelf.app.registration.presentation.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiObservers()
    }

    private fun apiObservers() {
        collectLatestLifecycleFlow(loginViewModel.signInButtonState) {
            binding.btnSignin.isEnabled = it
        }
    }

    override fun onResume() {
        super.onResume()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        binding.etUsername.doAfterTextChanged {
            if (isMinimum3chars(binding.etUsername.text.toString()).not()) {
                binding.tilUsername.error = getString(R.string.please_enter_minimum_3_characters)
                binding.tilUsername.isErrorEnabled = true
            } else {
                binding.tilUsername.isErrorEnabled = false
            }
            isAllFieldsValid()
        }

        binding.etPassword.doAfterTextChanged {
            if (loginViewModel.validatePassword(it.toString()).not()) {
                binding.tilPassword.error =
                    getString(R.string.password_must_contain_at_least_8_characters)
                binding.tilPassword.isErrorEnabled = true
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
            isAllFieldsValid()
        }
    }

    private fun isAllFieldsValid() {
        when {
            (binding.etUsername.text?.isEmpty() == true || binding.tilUsername.isErrorEnabled) -> {
                loginViewModel.updateSignInButtonState(false)
            }

            (binding.etPassword.text?.isEmpty() == true || binding.tilPassword.isErrorEnabled) -> {
                loginViewModel.updateSignInButtonState(false)
            }

            else -> loginViewModel.updateSignInButtonState(true)
        }
    }

    private fun isMinimum3chars(textField: String): Boolean {
        return textField.length >= 3
    }


}