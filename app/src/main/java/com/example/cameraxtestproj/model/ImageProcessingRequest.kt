package com.example.cameraxtestproj.model

data class ImageProcessingRequest(
    val image: String? = null,
    val unitId: String,
    val printerName: String,
    val userData: String,
    val messageId: String
)