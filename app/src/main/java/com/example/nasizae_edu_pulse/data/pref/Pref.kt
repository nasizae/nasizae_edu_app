package com.example.edupulse.data.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Pref(private val context: Context) {

    private val pref=context.getSharedPreferences("onBoardingPref",MODE_PRIVATE)

    fun isOnBoardingShow(): Boolean {
        return pref.getBoolean(SHOWED_KEY, false)
    }

    fun onOnBoardingShowed() {
        pref.edit().putBoolean(SHOWED_KEY, true).apply()
    }
    fun saveImage(image:String){
        pref.edit().putString(SAVE_IMAGE,image).apply()
    }
    
    companion object{
        const val SHOWED_KEY = "seen_key"
        const val SAVE_IMAGE = "save_image"
    }
}