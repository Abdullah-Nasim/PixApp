package com.android.pixapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.pixapp.R
import com.android.pixapp.databinding.ActivityRegisterBinding
import com.android.pixapp.viewmodels.RegisterViewModel
import java.lang.NumberFormatException

class RegisterActivity: AppCompatActivity(){

    lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by lazy {
        val activity = requireNotNull(this)
        ViewModelProvider(this, RegisterViewModel.Factory(activity.application))
            .get(RegisterViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.lifecycleOwner = this

        binding.viewModel = registerViewModel

        registerViewModel.mEmailLiveData.observe(this, Observer {
            if(!registerViewModel.isEmailValid(registerViewModel.mEmailLiveData.value!!))
                binding.emailAddress.error = "Enter Valid Email"
        })

        registerViewModel.mPasswordLiveData.observe(this, Observer {
            if(!registerViewModel.isPasswordValid(registerViewModel.mPasswordLiveData.value!!))
                binding.password.error = "Enter Valid Password"
        })

        registerViewModel.mConfirmPasswordLiveData.observe(this, Observer {
            if(!registerViewModel.isConfirmPassValid(registerViewModel.mConfirmPasswordLiveData.value!!))
                binding.confirmPassword.error = "Password and Confirm Password Mismatch"
        })

        registerViewModel.mAgeLivedata.observe(this, Observer {
            try{
                if(!registerViewModel.isValidAge(registerViewModel.mAgeLivedata.value!!.toInt()))
                    binding.age.error = "Enter Valid Age"
            }catch (ex: NumberFormatException){ Log.e("NumberFormatException", "Unable to parse Age to String")}
        })

        registerViewModel.mRegisterMediator.observe(this, Observer { validationResult ->
            binding.registerButton.isEnabled = validationResult
        })

    }
}