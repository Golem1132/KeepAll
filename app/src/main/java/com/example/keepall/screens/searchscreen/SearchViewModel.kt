package com.example.keepall.screens.searchscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.keepall.data.Note
import com.example.keepall.database.NoteDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel@Inject constructor(private val noteDao: NoteDao): ViewModel() {

    private val _result = MutableStateFlow<List<Note>>(emptyList())
    val result = _result.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(query.isBlank())
                _result.value = emptyList()
            else
                _result.value = noteDao.searchFor(query)
        }
    }
}