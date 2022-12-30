package com.example.gservices.utils

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val GSON = Gson()

    fun <T> saveData(context: Context, data: T, key: String) {
        val dataJson = GSON.toJson(data)
        sharedPreferences.edit().putString(key, dataJson).apply()
    }

    fun <T> getData(context: Context, key: String, dataClass: Class<T>): T? {
        val dataJson = sharedPreferences.getString(key, "")
        return if (dataJson.isNullOrEmpty()) {
            null
        } else {
            GSON.fromJson(dataJson, dataClass)
        }
    }

    fun clearData(context: Context, key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun clearAllSession(context: Context) {
        val editor = sharedPreferences.edit()
        sharedPreferences.all.keys.forEach { key ->
            editor.remove(key)
        }
        editor.apply()
    }

}