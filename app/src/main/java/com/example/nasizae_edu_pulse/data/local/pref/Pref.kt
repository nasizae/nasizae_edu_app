package com.example.edupulse.data.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE

class Pref(private val context: Context) {

    private val pref = context.getSharedPreferences("onBoardingPref", MODE_PRIVATE)

    fun isOnBoardingShow(): Boolean {
        return pref.getBoolean(SHOWED_KEY, false)
    }

    fun onOnBoardingShowed() {
        pref.edit().putBoolean(SHOWED_KEY, true).apply()
    }

    fun unlockSecondLevel() {
        pref.edit().putBoolean(NEXT_LEVEL_UNLOCKED_KEY, true).apply()
    }

    fun isNextLevelUnlocked(): Boolean {
        return pref.getBoolean(NEXT_LEVEL_UNLOCKED_KEY, false)
    }

    companion object {
        const val SHOWED_KEY = "seen_key"
        const val SAVE_IMAGE = "save_image"
        const val NEXT_LEVEL_UNLOCKED_KEY = "second_level_unlocked_key"
    }
}