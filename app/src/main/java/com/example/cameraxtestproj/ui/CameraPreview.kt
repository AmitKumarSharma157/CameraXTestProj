package com.example.cameraxtestproj.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController.IMAGE_ANALYSIS
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.cameraxtestproj.utils.Utils
import com.example.cameraxtestproj.viewmodel.CameraViewModel
import java.io.ByteArrayOutputStream

@Composable
fun CameraPreviewScreen(
    cameraViewModel: CameraViewModel
) {

    var isPrintLFCEnable by remember {
        mutableStateOf(true)
    }

    CameraPreview(
        cameraViewModel,
        isPrintLFCEnable
    ) { imageByteArray, unitId, printerName, userData, isEnable ->
        if (unitId != null && printerName != null && userData != null) {
            isPrintLFCEnable = isEnable
            cameraViewModel.processImage(imageByteArray, unitId, printerName, userData)
            if (cameraViewModel.status.value != null) {
                isPrintLFCEnable = true
            }
        }
    }
}

@Composable
fun CameraPreview(
    cameraViewModel: CameraViewModel,
    isPrintLFCEnable: Boolean,
    capturedImage: (imageByteArray: ByteArray, unitId: String?, printerName: String?, userData: String?, isPrintLFCEnable: Boolean) -> Unit
) {
    val context = LocalContext.current
    val unitId: String? = Utils.getUnitIdToPreferences(context)
    val printerName: String? = Utils.getPrinterNameToPreferences(context)
    val userData: String? = Utils.getUserDataPreferences(context)
    val controller = remember {
        LifecycleCameraController(context).apply {
            this.setEnabledUseCases(IMAGE_CAPTURE or IMAGE_ANALYSIS)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.6f)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { it ->
                    PreviewView(it).apply {
                        val cameraSelector = CameraSelector.Builder()
                            .requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
                        controller.cameraSelector = cameraSelector

                        this.controller = controller
                        controller.unbind()
                        controller.bindToLifecycle(lifecycleOwner)
                    }
                }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(.4f)
                .padding(20.dp, 80.dp,20.dp,20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(enabled = isPrintLFCEnable, onClick = {
                    takePhoto(controller, context) {
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        val isCompressed =
                            it.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream)
                        val imageByteArray = byteArrayOutputStream.toByteArray()
                        Log.i("TakePhoto", "Success: $isCompressed")
                        capturedImage(imageByteArray, unitId, printerName, userData, false)
                    }
                }) {
                    Text(text = "Print LFC", textAlign = TextAlign.Center)
                }
            }
            Text(modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 10.dp, 0.dp, 0.dp), text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black, fontSize = 20.sp)) {
                    append("LFC: ")
                }
                withStyle(style = SpanStyle(color = Color.Black, fontSize = 18.sp)) {
                    append(cameraViewModel.status.value)
                }
            })
        }
    }
}

private fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                val matrix = Matrix().apply {
                    this.postRotate(image.imageInfo.rotationDegrees.toFloat())
                }

                val rotateImageBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotateImageBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("TakePhoto", "Error: ${exception.message.toString()}")
            }
        }
    )
}