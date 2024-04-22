package com.example.cameraxtestproj.viewmodel

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cameraxtestproj.model.ImageProcessingRequest
import com.example.cameraxtestproj.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    ViewModel() {
    var status: MutableState<String> = mutableStateOf("")
    fun processImage(
        imageByteArray: ByteArray,
        unitId: String,
        printerName: String,
        userData: String
    ) {
        val encodedImage: String = Base64.encodeToString(imageByteArray, Base64.NO_WRAP)
        val messageId: String = UUID.randomUUID().toString()

        viewModelScope.launch {
            imageRepository.sendImage(
                (ImageProcessingRequest(
                    encodedImage,
                    unitId,
                    printerName,
                    userData,
                    messageId
                ))
            )
                .collect {
                    if (it.isSuccessful) {
                        if (it.body() != null) {
                            status.value = "${it.body()?.parsedID}"
                            Log.i(
                                "Success",
                                status.value
                            )
                        }
                    } else {
                        it.message()?.let { msg ->
                            Log.i(
                                "Failure",
                                msg
                            )
                        }
                    }
                }
        }
    }
}