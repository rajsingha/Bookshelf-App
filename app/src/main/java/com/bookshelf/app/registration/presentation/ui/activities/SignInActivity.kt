package com.bookshelf.app.registration.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.bookshelf.app.R
import com.bookshelf.app.core.baseui.BaseActivity
import com.bookshelf.app.core.utils.clickWithDebounce
import com.bookshelf.app.core.utils.collectLatestLifecycleFlow
import com.bookshelf.app.core.utils.showToast
import com.bookshelf.app.core.utils.validateEmail
import com.bookshelf.app.core.utils.validatePassword
import com.bookshelf.app.dashboard.presentation.ui.activities.DashboardActivity
import com.bookshelf.app.databinding.ActivitySignInBinding
import com.bookshelf.app.registration.data.models.LoginResult
import com.bookshelf.app.registration.data.models.SessionResult
import com.bookshelf.app.registration.presentation.utils.SessionManager
import com.bookshelf.app.registration.presentation.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : BaseActivity() {
    private lateinit var binding: ActivitySignInBinding
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        apiObservers()
    }

    private fun initView() {
        sessionManager = SessionManager(loginViewModel.useCase.sessionRepo)
    }

    private fun apiObservers() {
        collectLatestLifecycleFlow(loginViewModel.signInButtonState) {
            binding.btnSignin.isEnabled = it
        }

        collectLatestLifecycleFlow(loginViewModel.loginResult) {
            when (it) {
                is LoginResult.Success -> {
                    sessionManager.saveSession(it.user.email)
                }

                is LoginResult.Failure -> {
                    showToast(getString(R.string.please_enter_a_valid_email_password))
                }
            }
        }

        collectLatestLifecycleFlow(sessionManager.sessionObserver()) {
            when (it) {
                is SessionResult.Active -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }

                is SessionResult.NotActive -> {
                    showToast("Not Active")
                }
            }
        }
    }

    override fun setOnclickListener() {
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

        binding.tvSignup.clickWithDebounce {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.btnSignin.clickWithDebounce {
            loginViewModel.login(
                binding.etUserEmail.text.toString(),
                binding.etPassword.text.toString()
            )
        }
    }

    private fun isAllFieldsValid() {
        when {
            (binding.etUserEmail.text?.isEmpty() == true || binding.tilUserEmail.isErrorEnabled) -> {
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
