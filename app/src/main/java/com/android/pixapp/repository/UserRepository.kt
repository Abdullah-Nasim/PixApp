package com.android.pixapp.repository

import com.android.pixapp.database.PixAppDatabase
import com.android.pixapp.database.asDomainModel
import com.android.pixapp.domain.PixAppUser
import com.android.pixapp.domain.asDatabaseModel

class UserRepository(private val database: PixAppDatabase){

    fun createUser(user: PixAppUser){
        database.userDao.insert(user.asDatabaseModel())
    }

    fun findUser(email: String): PixAppUser{
        return database.userDao.getUser(email).asDomainModel()
    }

}