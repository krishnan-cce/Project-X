package com.example.gservices.base

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.gservices.utils.SessionManager
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}