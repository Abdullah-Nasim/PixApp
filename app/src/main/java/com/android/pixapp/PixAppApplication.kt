package com.android.pixapp

import android.app.Application
import com.android.pixapp.utils.AppSharedPreferences

class PixAppApplication : Application() {

    /**
     * onCreate is called before the first screen is shown to the user
     * I will initialize application level dependencies here
     */
    override fun onCreate() {
        super.onCreate()
        AppSharedPreferences.init(this)
    }
}
