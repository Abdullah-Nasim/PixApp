package com.android.pixapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.pixapp.R
import com.android.pixapp.databinding.ActivityLoginBinding
import com.android.pixapp.viewmodels.LoginViewModel

class LoginActivity: AppCompatActivity(){

    lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by lazy {
        val activity = requireNotNull(this)
        ViewModelProvider(this, LoginViewModel.Factory(activity.application))
            .get(LoginViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.lifecycleOwner = this

        binding.viewModel = loginViewModel

        loginViewModel.mEmailLiveData.observe(this, Observer {
            if(!loginViewModel.isEmailValid(loginViewModel.mEmailLiveData.value!!))
                binding.emailAddress.error = "Enter Valid Email"
        })

        loginViewModel.mPasswordLiveData.observe(this, Observer {
            if(!loginViewModel.isPasswordValid(loginViewModel.mPasswordLiveData.value!!))
                binding.password.error = "Enter Valid Password"
        })

        loginViewModel.mLoginPasswordMediator.observe(this, Observer { validationResult ->
            binding.loginButton.isEnabled = validationResult
        })

        loginViewModel.mOpenScreenLiveData.observe(this, Observer {
            if(it == "OPEN_REGISTER_SCREEN"){
                startActivity(Intent(this, RegisterActivity::class.java))
            } else if(it == "OPEN_MAIN_SCREEN"){
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        loginViewModel.mShowToast.observe(this, Observer {
            Toast.makeText(this, it,Toast.LENGTH_SHORT).show()
        })

    }

}