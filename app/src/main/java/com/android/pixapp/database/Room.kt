package com.android.pixapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PictureDao {
    @Query("select * from databasepicture")
    fun getPictures(): LiveData<List<DatabasePicture>>

    @Query("select * from databasepicture where id=:id")
    fun getPicture(id: Int): DatabasePicture

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( videos: List<DatabasePicture>)
}

@Dao
interface UserDao {
    @Query("select * from databaseuser where email=:email")
    fun getUser(email: String): DatabaseUser

    @Insert
    fun insert(user: DatabaseUser)
}

@Database(entities = [DatabasePicture::class, DatabaseUser::class], version = 1)
abstract class PixAppDatabase: RoomDatabase() {
    abstract val pictureDao: PictureDao
    abstract val userDao: UserDao
}

private lateinit var INSTANCE: PixAppDatabase

fun getDatabase(context: Context): PixAppDatabase {
    synchronized(PixAppDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                PixAppDatabase::class.java,
                "pixappdb").build()
        }
    }
    return INSTANCE
}
