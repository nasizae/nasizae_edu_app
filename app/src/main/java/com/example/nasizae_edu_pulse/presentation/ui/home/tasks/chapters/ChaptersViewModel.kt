package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nasizae_edu_pulse.data.model.Chapter
import com.example.nasizae_edu_pulse.data.model.ChapterModel
import com.example.nasizae_edu_pulse.domain.repository.RepositoryImpl
import kotlinx.coroutines.launch

class ChaptersViewModel(private val repositoryImpl: RepositoryImpl) : ViewModel() {
    private val _chapters = MutableLiveData<Chapter>()
    val chapters: LiveData<Chapter> get() = _chapters

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    init {
        fetchGetDataChapter()
    }

    private fun fetchGetDataChapter() {
        _loading.value = true
        viewModelScope.launch {
            val chaptersData = repositoryImpl.fetchGetDataChapters()
            chaptersData.observeForever { chapter ->
                _chapters.value = chapter
                _loading.value = false
            }
        }
    }

}