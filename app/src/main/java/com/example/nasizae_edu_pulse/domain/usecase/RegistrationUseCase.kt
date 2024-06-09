package com.example.edupulse.domain.usecase

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import com.example.edupulse.data.model.Users
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.AlertdialogExitAccountBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage

class RegistrationUseCase(private val context:Context, private val navController: NavController,private val filePath:Uri,private val layoutInflater: LayoutInflater) {
    private val myDataBase = Firebase.database.getReference(USER)
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private lateinit var alertDialog: AlertDialog
    fun registration(fullName: String, email: String, password: String,) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && filePath != null) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    val user: FirebaseUser? = auth.currentUser
                    val uid = user?.uid
                    if (uid != null) {
                        user.sendEmailVerification().addOnSuccessListener {
                                createAlertDialog(uid,fullName,email,password)
                                    alertDialog.show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                            }
                    }
                }

            }
        } else {
            Toast.makeText(context, "Заполните данные", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAlertDialog(
        uid: String,
        fullName: String,
        email: String,
        password: String
    ) {
        val user:FirebaseUser?=auth.currentUser
        val alertDialogBuilder=AlertDialog.Builder(context,R.style.CustomAlertDialogStyle)
        val alertDialogBinding=AlertdialogExitAccountBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertDialogBinding.root)
        alertDialog=alertDialogBuilder.create()
        alertDialogBinding.btnYes.setOnClickListener {
            if (user != null) {
                user.reload().addOnSuccessListener {
                    if (user.isEmailVerified) {
                        setDataUser(uid, fullName, email, password)
                        alertDialog.dismiss()
                    }
                }
                    .addOnFailureListener {
                        Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun setDataUser(uid: String, fullName: String, email: String, password: String) {
                    storage.getReference().child("image/" + uid).putFile(filePath)
                        .addOnCompleteListener {
                            storage.getReference().child("image/" + uid).downloadUrl
                                .addOnSuccessListener {
                                    val userData =
                                        Users(uid, fullName, email, password, it.toString())
                                    myDataBase.child(uid).setValue(userData).addOnSuccessListener {
                                        navController.navigate(R.id.homeScreenFragment)
                                    }
                                }
                        }


    }

    companion object {
        const val USER = "Users"
    }
}