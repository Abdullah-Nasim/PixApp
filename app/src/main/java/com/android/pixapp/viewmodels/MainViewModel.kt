package com.android.pixapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.pixapp.database.getDatabase
import com.android.pixapp.repositories.PicturesRepository
import com.android.pixapp.utils.AppSharedPreferences
import com.android.pixapp.utils.SingleLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * This view model stores the and manages the UI-Related data for MainActivity
 */
class MainViewModel(application: Application): ViewModel(){

    private val picturesRepository = PicturesRepository(getDatabase(application))

    val pictures = picturesRepository.pictures

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    var eventNetworkError = MutableLiveData(false)

    var eventOpenScreenLiveData  = SingleLiveData<String>()

    var eventProgressShown = MutableLiveData<Boolean>()

    init {
        refreshDataFromRepository()
    }

    private fun refreshDataFromRepository() {

        if(pictures.value == null || pictures.value!!.isEmpty())
            eventProgressShown.value = true

        viewModelScope.launch {
            try {
                picturesRepository.refreshPictures()
                eventNetworkError.value = false

            } catch (networkError: IOException) {
                eventNetworkError.value = true
            }
        }.invokeOnCompletion {
            eventProgressShown.value = false
        }
    }

    fun logoutUser(){
        AppSharedPreferences.saveUserLoginState("")
        eventOpenScreenLiveData.value = "OPEN_LOGIN_SCREEN"
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}