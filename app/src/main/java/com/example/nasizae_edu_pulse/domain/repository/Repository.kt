package com.example.nasizae_edu_pulse.domain.repository

import androidx.lifecycle.LiveData
import com.example.edupulse.data.model.Users
import com.example.nasizae_edu_pulse.data.model.Chapter
import com.example.nasizae_edu_pulse.data.model.ChapterModel
import com.google.firebase.auth.FirebaseAuth

interface Repository {

    fun fetchGetUserData(auth: FirebaseAuth):LiveData<Users>
    fun fetchGetDataChapters():LiveData<Chapter>
}