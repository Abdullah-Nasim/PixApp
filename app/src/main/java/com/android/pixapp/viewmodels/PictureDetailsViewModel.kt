package com.android.pixapp.viewmodels

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.pixapp.database.getDatabase
import com.android.pixapp.repositories.PicturesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class PictureDetailsViewModel(application: Application, private val pictureId: Int): ViewModel(){

    val mPictureUrlLiveData = MutableLiveData<String>()

    val mPictureUploaderLiveData = MutableLiveData<String>()

    val mPictureSizeLiveData = MutableLiveData<String>()

    val mPictureTypeLiveData = MutableLiveData<String>()

    val mPictureTagsLiveData = MutableLiveData<String>()

    val mPictureViewsLiveData = MutableLiveData<String>()

    val mPictureLikesLiveData = MutableLiveData<String>()

    val mPictureCommentsLiveData = MutableLiveData<String>()

    val mPictureFavoritesLiveData = MutableLiveData<String>()

    val mPictureDownloadsLiveData = MutableLiveData<String>()

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val picturesRepository = PicturesRepository(getDatabase(application))

    init {
        getProductDetailsData()
    }

    private fun getProductDetailsData() {
        viewModelScope.launch {
            val picture = picturesRepository.getStoredPicture(pictureId = pictureId)
            if(picture != null){
                mPictureUrlLiveData.value = picture.url
                mPictureUploaderLiveData.value = picture.uploader
                mPictureSizeLiveData.value = picture.imageSize.toString()
                mPictureTypeLiveData.value = picture.type
                mPictureTagsLiveData.value = picture.tags
                mPictureViewsLiveData.value = picture.views.toString()
                mPictureLikesLiveData.value = picture.likes.toString()
                mPictureCommentsLiveData.value = picture.comments.toString()
                mPictureFavoritesLiveData.value = picture.favorites.toString()
                mPictureDownloadsLiveData.value = picture.downloads.toString()
            }
        }
    }


    /**
     * Factory for constructing PictureDetailsViewModel with parameters
     */
    class Factory(private val app: Application, private val pictureId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PictureDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PictureDetailsViewModel(app, pictureId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}