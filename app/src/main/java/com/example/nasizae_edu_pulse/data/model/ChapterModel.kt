package com.example.nasizae_edu_pulse.data.model

import com.google.firebase.database.PropertyName
    data class ChapterModel(
        @get:PropertyName("chapterName")
        val chapterName:String?=null,
        @get:PropertyName("course")
        val course:String?=null
    )
data class Chapter(
    val chapter:List<ChapterModel> = emptyList()
)
