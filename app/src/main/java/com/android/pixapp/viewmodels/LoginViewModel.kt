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
import com.android.pixapp.utils.SingleLiveData
import kotlinx.coroutines.*

/**
 * This view model stores the and manages the UI-Related data for LoginActivity
 */
class LoginViewModel(application: Application): ViewModel() {

    val mEmailLiveData = MutableLiveData<String>()

    val mPasswordLiveData = MutableLiveData<String>()

    val mShowToast = MutableLiveData<String>()

    val mLoginPasswordMediator = MediatorLiveData<Boolean>()

    var mOpenScreenLiveData  = SingleLiveData<String>()

    private val userRepository = UserRepository(getDatabase(application))

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        mLoginPasswordMediator.value = false
        mLoginPasswordMediator.addSource(mEmailLiveData) { validateForm() }
        mLoginPasswordMediator.addSource(mPasswordLiveData) { validateForm() }
    }

    private fun validateForm() {
        if(mEmailLiveData.value != null && mPasswordLiveData.value != null)
            mLoginPasswordMediator.value = isEmailValid(mEmailLiveData.value!!)
                    && isPasswordValid(mPasswordLiveData.value!!)
    }

    /**
     * This method is used to validate the email
     */
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * This method is used to validate the password
     */
    fun isPasswordValid(password: String): Boolean {
        return password.length in 6..12
    }

    /**
     * This method is used to handle the click event performed in the clickable portions of LoginActivity
     */
    fun onClick(view: View) {
        if(view.id == R.id.loginButton){
            viewModelScope.launch {
                val user = loginUser()
                when {
                    user == null -> mShowToast.value = "User not found"
                    user.password == mPasswordLiveData.value!! -> mOpenScreenLiveData.value = "OPEN_MAIN_SCREEN"
                    else -> mShowToast.value = "Password is incorrect"
                }
            }
        }else if(view.id == R.id.registerButton){
            mOpenScreenLiveData.value = "OPEN_REGISTER_SCREEN"
        }
    }

    /**
     * This method is used to find the user from database if the user exists it
     * will return PixAppUser otherwise null
     */
    private suspend fun loginUser(): PixAppUser?{
        var user: PixAppUser? = null
        withContext(Dispatchers.IO){
            try{
                user = userRepository.findUser(mEmailLiveData.value!!)
            } catch (ex: Exception){
                return@withContext null
            }
        }
        return user
    }

    override fun onCleared() {
        mLoginPasswordMediator.removeSource(mEmailLiveData)
        mLoginPasswordMediator.removeSource(mPasswordLiveData)
    }

    /**
     * Factory for constructing DevByteViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}