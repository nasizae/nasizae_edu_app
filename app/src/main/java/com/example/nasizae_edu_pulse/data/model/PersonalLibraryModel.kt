package com.example.nasizae_edu_pulse.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonalLibraryModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val position:Int?=null,
    val libraryName:String?=null
)
