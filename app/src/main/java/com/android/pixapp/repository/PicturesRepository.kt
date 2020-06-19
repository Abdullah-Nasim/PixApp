package com.android.pixapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.android.pixapp.database.PixAppDatabase
import com.android.pixapp.database.asDomainModel
import com.android.pixapp.domain.PixAppPicture
import com.android.pixapp.network.PixAppNetwork
import com.android.pixapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repository for fetching pixbay pictures from the network and storing them on disk
 */
class PicturesRepository(private val database: PixAppDatabase) {

    val pictures: LiveData<List<PixAppPicture>> = Transformations.map(database.pictureDao.getPictures()) {
        it.asDomainModel()
    }

    /**
     * Refresh the pictures stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshPictures() {
        withContext(Dispatchers.IO) {
            val pictures = PixAppNetwork.pixBay.getPictures().await()
            database.pictureDao.insertAll(pictures.asDatabaseModel())
        }
    }

}
