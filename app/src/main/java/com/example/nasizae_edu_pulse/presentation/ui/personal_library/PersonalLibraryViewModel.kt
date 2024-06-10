package com.example.nasizae_edu_pulse.presentation.ui.personal_library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.nasizae_edu_pulse.data.local.room.LibraryDatabase
import com.example.nasizae_edu_pulse.data.model.PersonalLibraryModel
import kotlinx.coroutines.launch

class PersonalLibraryViewModel(val database: LibraryDatabase):ViewModel() {

    val allItems=database.libraryDao().getAll()

    fun search(query: String,callBack:(List<PersonalLibraryModel>)->Unit){
        viewModelScope.launch {
          val items=database.libraryDao().search(query)
            callBack(items)
        }
    }

}