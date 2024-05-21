package com.example.nasizae_edu_pulse

import android.app.Application
import androidx.room.Room
import com.example.nasizae_edu_pulse.data.local.room.LibraryDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        personalLibraryDataBase = Room.databaseBuilder(
            applicationContext,
            LibraryDatabase::class.java,
            "personal_library"
        ).allowMainThreadQueries().build()
    }
    companion object {
        lateinit var personalLibraryDataBase: LibraryDatabase
    }
}