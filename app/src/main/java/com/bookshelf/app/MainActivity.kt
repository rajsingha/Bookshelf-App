package com.bookshelf.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bookshelf.app.core.utils.collectLatestLifecycleFlow
import com.bookshelf.app.registration.data.models.SignupResult
import com.bookshelf.app.registration.presentation.utils.SessionManager
import com.bookshelf.app.registration.presentation.viewmodels.LoginViewModel
import com.bookshelf.app.registration.presentation.viewmodels.SignupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private val signupViewModel: SignupViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        sessionManager = SessionManager(signupViewModel.useCase.sessionRepo)
        collectLatestLifecycleFlow(sessionManager.sessionObserver()){

        }
        runBlocking {
            sessionManager.clearSession()
        }
        collectLatestLifecycleFlow(signupViewModel.signupResult){
            when(it){

                SignupResult.Failure -> {
                    Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()

                }

                SignupResult.Success -> {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()


                }

                SignupResult.UserEmailTaken -> {
                    Toast.makeText(this, "Taken", Toast.LENGTH_SHORT).show()
                }
            }
        }


        signupViewModel.signup("rajsingha", "rajsingha@gmail.com", "Raj@4520", "India")

    }
}