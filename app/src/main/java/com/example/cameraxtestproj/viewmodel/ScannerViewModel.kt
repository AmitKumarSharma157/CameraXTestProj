package com.example.cameraxtestproj.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraxtestproj.model.ScannedCode
import com.example.cameraxtestproj.repository.ScannersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(private val repository: ScannersRepository) :
    ViewModel() {
    var scannedCode = mutableStateOf<ScannedCode?>(null)

    init {
        repository.initDataWedge()
        viewModelScope.launch {
            repository.dataWedgeScannedCode().collect {
                scannedCode.value = it
            }
        }
    }
}