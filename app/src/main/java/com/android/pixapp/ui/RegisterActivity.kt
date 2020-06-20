package com.android.pixapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.pixapp.R
import com.android.pixapp.databinding.ActivityRegisterBinding
import com.android.pixapp.viewmodels.RegisterViewModel
import java.lang.NumberFormatException

/**
 * The responsibility of this activity is to display the registration screen
 */
class RegisterActivity: AppCompatActivity(){

    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel: RegisterViewModel by lazy {
        val activity = requireNotNull(this)
        ViewModelProvider(this, RegisterViewModel.Factory(activity.application))
            .get(RegisterViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        // Setting the lifecycleOwner so DataBinding can observe LiveData
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

        registerViewModel.mAgeLiveData.observe(this, Observer {
            try{
                if(!registerViewModel.isValidAge(registerViewModel.mAgeLiveData.value!!.toInt()))
                    binding.age.error = "Enter Valid Age"
            }catch (ex: NumberFormatException){ Log.e("NumberFormatException", "Unable to parse Age to String")}
        })

        registerViewModel.mRegisterMediator.observe(this, Observer { validationResult ->
            binding.registerButton.isEnabled = validationResult
        })

        registerViewModel.eventOpenScreenLiveData.observe(this, Observer {
            if(it == "OPEN_MAIN_SCREEN"){ startActivity(Intent(this, MainActivity::class.java)) }
        })

        registerViewModel.eventShowToastLiveData.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

    }
}