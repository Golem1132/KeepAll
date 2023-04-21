package com.example.keepall.screens.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images = _images.asStateFlow()

    private val _imagesChecked = MutableStateFlow<List<String>>(emptyList())
    val imagesChecked = _imagesChecked.asStateFlow()

    fun addFetchedImage(path: String) {
        viewModelScope.launch {
            flow {
                emit(path)
            }.collect {
                val tempList = mutableListOf(it)
                tempList.addAll(_images.value)
                _images.value = tempList
            }
        }
    }

    fun putImageToCheckedList(path: String) {
        viewModelScope.launch {
            flow {
                emit(path)
            }.collect {
                val tempList: MutableList<String> = mutableListOf()
                if (_imagesChecked.value.contains(it)) {
                    tempList.addAll(_imagesChecked.value)
                    tempList.remove(it)
                    _imagesChecked.value = tempList
                } else {
                    tempList.add(it)
                    tempList.addAll(_imagesChecked.value)
                    _imagesChecked.value = tempList
                }
            }
        }
    }

    fun clearCheckedImages() {
        viewModelScope.launch {
            flow<List<String>> {
                emit(emptyList())
            }.collect {
                _imagesChecked.value = it
            }
        }
    }
}