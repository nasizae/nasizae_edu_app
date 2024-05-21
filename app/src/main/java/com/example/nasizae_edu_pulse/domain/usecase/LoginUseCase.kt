package com.example.edupulse.domain.usecase

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.navigation.NavController
import com.example.nasizae_edu_pulse.R
import com.google.firebase.auth.FirebaseAuth

class LoginUseCase(private val context: Context,private val navController: NavController) {
    private val auth = FirebaseAuth.getInstance()
    fun login(email: String, password: String) {
        if (!TextUtils.isEmpty(password) &&
            !TextUtils.isEmpty(email)
        ) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    navController.navigate(R.id.homefragment)
                }
                else{
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}