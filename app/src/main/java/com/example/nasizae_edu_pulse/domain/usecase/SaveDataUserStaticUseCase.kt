package com.example.nasizae_edu_pulse.domain.usecase

import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.domain.model.UserStaticModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.database

class SaveDataUserStaticUseCase {

    private val auth = FirebaseAuth.getInstance()
    private val myDatabase = Firebase.database.getReference(USER)

    fun save(
        userLvl: Int,
        progressNumber: Int? = null,
        maxUserLvl: Int,
        maxCountProgress: Int,
        nameThemeWork: String,
    ) {
        val user: FirebaseUser? = auth.currentUser
        val uid: String? = user?.uid
        if (uid != null) {
            val userStatic = UserStaticModel(
                userLvl = userLvl,
                progressNumber = progressNumber,
                maxUserLvl = maxUserLvl,
                maxCountProgress = maxCountProgress,
                nameThemeWork = nameThemeWork
            )
            myDatabase.child(uid).child("static").setValue(userStatic)
        }
    }
}