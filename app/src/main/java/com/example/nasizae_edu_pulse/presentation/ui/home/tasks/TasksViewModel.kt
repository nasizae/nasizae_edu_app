package com.example.edupulse.presentation.ui.home.tasks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.domain.repository.Repository
import com.example.nasizae_edu_pulse.domain.repository.RepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class TasksViewModel(private val repository: RepositoryImpl):ViewModel() {
    private val auth=FirebaseAuth.getInstance()
    private val _users=MutableLiveData<Users>()
    val users:LiveData<Users> get() = _users

    private val _loading=MutableLiveData<Boolean>()
    val loading:LiveData<Boolean> get() = _loading
    private val _error=MutableLiveData<String>()
    val error:LiveData<String> get() = _error

    init {
        fetchUserData(auth)
    }

    private fun fetchUserData(auth: FirebaseAuth) {
        _loading.value=true
        viewModelScope.launch {
            val userData = repository.fetchGetUserData(auth)
            userData.observeForever { user ->
                _users.value=user
                _loading.value=false
            }
        }
    }

}