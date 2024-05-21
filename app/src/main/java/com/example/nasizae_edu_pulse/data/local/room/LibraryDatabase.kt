package com.example.nasizae_edu_pulse.data.local.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nasizae_edu_pulse.data.model.PersonalLibraryModel

@Database(entities = [PersonalLibraryModel::class], version = 1)
abstract class LibraryDatabase:RoomDatabase(){
abstract fun libraryDao():LibraryDao
}