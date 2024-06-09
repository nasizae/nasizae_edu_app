package com.example.nasizae_edu_pulse.domain.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.data.model.Chapter
import com.example.nasizae_edu_pulse.data.model.ChapterModel
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.ChaptersFragment.Companion.CHAPTER_COLLECTION
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.ChaptersFragment.Companion.CHAPTER_DOCUMENT
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.ChaptersFragment.Companion.CHAPTER_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class RepositoryImpl : Repository {
    private val myDataBase = Firebase.database.getReference(USER)
    private val fireStore = FirebaseFirestore.getInstance()
    override fun fetchGetUserData(auth: FirebaseAuth): LiveData<Users> {
        val user = MutableLiveData<Users>()
        val uid = auth.currentUser?.uid.toString()
        myDataBase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Users::class.java)
                user.value = value!!
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
        return user
    }

    override fun fetchGetDataChapters(): LiveData<Chapter> {
        val chapters = MutableLiveData<Chapter>()
        fireStore.collection(CHAPTER_COLLECTION).document(CHAPTER_DOCUMENT).get()
            .addOnSuccessListener {
                if(it!=null) {
                    val value = it.toObject(Chapter::class.java)
                    chapters.value = value!!
                }
            }
            .addOnFailureListener {
                Log.e("ololo", "fetchGetDataChapters:$it", )
            }
        return chapters
    }
}