package com.android.pixapp.viewmodels

import android.app.Application
import android.util.Patterns
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.pixapp.R
import com.android.pixapp.database.getDatabase
import com.android.pixapp.domain.PixAppUser
import com.android.pixapp.repository.UserRepository
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * This view model stores the and manages the UI-Related data for RegisterActivity
 */
class RegisterViewModel(application: Application): ViewModel(){

    val mEmailLiveData = MutableLiveData<String>()

    val mPasswordLiveData = MutableLiveData<String>()

    val mConfirmPasswordLiveData = MutableLiveData<String>()

    val mAgeLivedata = MutableLiveData<String>()

    val mRegisterMediator = MediatorLiveData<Boolean>()

    private val userRepository = UserRepository(getDatabase(application))

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        mRegisterMediator.value = false
        mRegisterMediator.addSource(mEmailLiveData) { validateForm() }
        mRegisterMediator.addSource(mPasswordLiveData) { validateForm() }
        mRegisterMediator.addSource(mConfirmPasswordLiveData) { validateForm() }
        mRegisterMediator.addSource(mAgeLivedata){ validateForm() }
    }

    private fun validateForm() {

        if(mEmailLiveData.value != null && mPasswordLiveData.value != null
            && mConfirmPasswordLiveData.value != null && mAgeLivedata.value != null)

            mRegisterMediator.value = isEmailValid(mEmailLiveData.value!!)
                    && isPasswordValid(mPasswordLiveData.value!!)
                    && isConfirmPassValid(mConfirmPasswordLiveData.value!!)
                    && isValidAge(mAgeLivedata.value!!.toInt())

    }

    /**
     * This method is used to validate email
     */
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * This method is used to validate password
     */
    fun isPasswordValid(password: String): Boolean {
        return password.length in 6..12
    }

    /**
     * This method is used to validate confirm password
     */
    fun isConfirmPassValid(password: String): Boolean{
        return mPasswordLiveData.value!! == password
    }

    /**
     * This method is used to validate age
     */
    fun isValidAge(age: Int): Boolean{
        return age in 19..87
    }

    /**
     * This method is used to handle the click event performed in the clickable portions of RegisterActivity
     */
    fun onClick(view: View) {
        if(view.id == R.id.registerButton){
            val registerUser = PixAppUser(mEmailLiveData.value!!, mPasswordLiveData.value!!, mAgeLivedata.value!!.toInt())
            viewModelScope.launch { createUser(registerUser) }
        }
    }

    /**
     * This method is used to create a new user in database
     */
    private suspend fun createUser(registerUser: PixAppUser) {
        try{
            withContext(Dispatchers.IO){
                userRepository.createUser(registerUser)
            }
        }catch (ex: Exception){
            ex.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mRegisterMediator.removeSource(mEmailLiveData)
        mRegisterMediator.removeSource(mPasswordLiveData)
        mRegisterMediator.removeSource(mConfirmPasswordLiveData)
        mRegisterMediator.removeSource(mAgeLivedata)
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}