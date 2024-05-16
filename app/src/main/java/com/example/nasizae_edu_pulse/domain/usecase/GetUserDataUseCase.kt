package com.example.edupulse.domain.usecase

import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class GetUserDataUseCase() {
    private val auth = FirebaseAuth.getInstance()
    private val myDatabase = Firebase.database.getReference(USER)

    interface CallBack {
        fun onUserReceived(users: Users)
        fun onError(error: String)
    }

    fun getUser(callBack: CallBack) {
        val user: FirebaseUser? = auth.currentUser
        val uid: String? = user?.uid
        if (uid != null) {
            myDatabase.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(Users::class.java)
                    value?.let {
                        callBack.onUserReceived(it)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    callBack.onError(error.message)
                }
            })
        }
    }
}