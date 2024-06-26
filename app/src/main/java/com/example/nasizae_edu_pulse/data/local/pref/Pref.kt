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

    fun alertDialogShow(){
        pref.edit().putBoolean(KEY_ALERT_SHOW,true).apply()
    }
    fun alertDialogShowed():Boolean{
       return pref.getBoolean(KEY_ALERT_SHOW,false)
    }


    fun isQuestionnaireShow(): Boolean {
        return pref.getBoolean(QUESTIONNAIRE_SHOW, false)
    }

    fun onQuestionnaireShowed() {
        pref.edit().putBoolean(QUESTIONNAIRE_SHOW, true).apply()
    }
    fun isJavaOrKotlinShow(): Boolean {
        return pref.getBoolean(JAVAORKOTLIN_SHOW, false)
    }

    fun onJavaOrKotlinShowed() {
        pref.edit().putBoolean(JAVAORKOTLIN_SHOW, true).apply()
    }

    companion object {
        const val SHOWED_KEY = "seen_key"
        const val KEY_ALERT_SHOW="alert_show"
        const val QUESTIONNAIRE_SHOW="questionnaire"
        const val JAVAORKOTLIN_SHOW="javaorkotlin"
    }
}