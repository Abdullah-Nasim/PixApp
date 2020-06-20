package com.android.pixapp.utils

import android.content.Context
import android.content.SharedPreferences

object AppSharedPreferences{

    private const val USER_EMAIL_PREF_KEY: String = "USER_EMAIL_PREF_KEY"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }

    fun saveUserLoginState(email: String) {
        val editor: SharedPreferences.Editor
        try {
            editor = prefs.edit()
            editor.putString(USER_EMAIL_PREF_KEY, email)
            editor.apply()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getUserLoginState(): String?{
        return prefs.getString(USER_EMAIL_PREF_KEY, "")
    }


}