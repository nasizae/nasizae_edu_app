package com.example.nasizae_edu_pulse.data.model

import com.google.firebase.database.PropertyName
    data class ChapterModel(
        @get:PropertyName("chaptersName")
        val chaptersName:ArrayList<String>?=null,
        @get:PropertyName("course")
        val course:ArrayList<String>?=null
    )
