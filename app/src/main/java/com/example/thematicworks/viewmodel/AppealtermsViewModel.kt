package com.example.thematicworks.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppealtermsViewModel : ViewModel() {
    val ttthing1: MutableLiveData<String> by lazy { MutableLiveData<String>() }
}
// 這裡似乎可以不用LiveData