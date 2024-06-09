package com.example.nasizae_edu_pulse.domain.usecase

import com.example.edupulse.domain.usecase.GetUserDataUseCase
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.domain.model.UserDataStaticResult
import com.example.nasizae_edu_pulse.domain.model.UserDataStaticTasks
import com.example.nasizae_edu_pulse.domain.model.UserStaticModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class GetUserStaticUseCase {

    interface Call{
        fun getDataStaticTasks(userStaticTasks:UserDataStaticTasks)
        fun getDataStaticResult(userStaticResult:UserDataStaticResult)
        fun error(error:String)

    }

    private val auth=FirebaseAuth.getInstance()
    private val myDataBase= Firebase.database.getReference(USER)

    fun getUserStatic(call:Call){
        val user:FirebaseUser?=auth.currentUser
        val uid:String?=user?.uid
        if(uid!=null){
            myDataBase.child(uid).child("static").child("static_in_result").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value=snapshot.getValue(UserDataStaticResult::class.java)
                    if(value!=null){
                        call.getDataStaticResult(value)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                call.error(error.message)
                }

            })
            myDataBase.child(uid).child("static").child("static_in_tasks").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value=snapshot.getValue(UserDataStaticTasks::class.java)
                    if(value!=null){
                        call.getDataStaticTasks(value)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }


}