package com.example.edupulse.domain.usecase

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.navigation.NavController
import com.example.edupulse.data.model.Users
import com.example.nasizae_edu_pulse.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage

class RegistrationUseCase(private val context:Context, private val navController: NavController,private val filePath:Uri) {
    private val myDataBase = Firebase.database.getReference(USER)
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    fun registration(fullName: String, email: String, password: String) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val uid: String? = user?.uid
                    if (uid != null) {
                        setDataUser(uid, fullName, email, password)
                        navController.navigate(R.id.homefragment)
                    } else {
                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setDataUser(uid: String, fullName: String, email: String, password: String) {
        if (uid != null) {
            storage.getReference().child("image/" + uid).putFile(filePath)
                .addOnCompleteListener {
                    storage.getReference().child("image/" + uid).downloadUrl
                        .addOnSuccessListener {
                            val user = Users(uid, fullName, email, password, it.toString())
                            myDataBase.child(uid).setValue(user)
                        }
                }
        }
    }
    companion object {
        const val USER = "Users"
    }
}