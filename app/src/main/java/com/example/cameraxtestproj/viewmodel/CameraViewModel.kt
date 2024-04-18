package com.example.cameraxtestproj.viewmodel

import android.graphics.Bitmap
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
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    ViewModel() {
    var status: MutableState<String> = mutableStateOf("")
    fun processImage(bitmap: Bitmap, unitId: String, printerName: String, userData: String) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
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
                            Log.d(
                                "Success",
                                status.value
                            )
                        }
                    } else {
                        it.message()?.let { msg ->
                            Log.d(
                                "Failure",
                                msg
                            )
                        }
                    }
                }
        }
    }
}