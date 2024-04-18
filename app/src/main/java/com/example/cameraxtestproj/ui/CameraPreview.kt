package com.example.cameraxtestproj.ui

import LFCAlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Size
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.CameraController.IMAGE_ANALYSIS
import androidx.camera.view.CameraController.IMAGE_CAPTURE
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.cameraxtestproj.R
import com.example.cameraxtestproj.Utils.Utils
import com.example.cameraxtestproj.viewmodel.CameraViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun CameraPreviewScreen(
    cameraViewModel: CameraViewModel,
    navController: NavController
) {
    val openDialog = remember {
        mutableStateOf(true)
    }
    CameraPreview(
        cameraViewModel,
        openDialog.value
    ) { image, unitId, printerName, userData, isOpen ->
        openDialog.value = isOpen
        if (unitId != null && printerName != null && userData != null) {
            cameraViewModel.processImage(image, unitId, printerName, userData)
        }
    }
}

@Composable
fun CameraPreview(
    cameraViewModel: CameraViewModel,
    isDialogOpen: Boolean,
    capturedImage: (image: Bitmap, unitId: String?, printerName: String?, userData: String?, isOpen: Boolean) -> Unit
) {
    val context = LocalContext.current
    val unitId: String? = Utils.getUnitIdToPreferences(context)
    val printerName: String? = Utils.getPrinterNameToPreferences(context)
    val userData: String? = Utils.getUserDataPreferences(context)
    var isDialogOpen: Boolean = isDialogOpen
    val controller = remember {
        LifecycleCameraController(context).apply {
            this.setEnabledUseCases(IMAGE_CAPTURE or IMAGE_ANALYSIS)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { it ->
            PreviewView(it).apply {
                controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                controller.imageCaptureTargetSize =
                    CameraController.OutputSize(AspectRatio.RATIO_4_3)
                controller.imageCaptureTargetSize = CameraController.OutputSize(Size(200, 200))
                controller.setZoomRatio(.1f)
                this.controller = controller
                controller.unbind()
                controller.bindToLifecycle(lifecycleOwner)
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        IconButton(onClick = {
            takePhoto(controller, context) {
                val path = context.getExternalFilesDir(null)?.absolutePath
                val tempFile = File(path, "tempFileName.jpg")
                val fOut = FileOutputStream(tempFile)
                it.compress(Bitmap.CompressFormat.JPEG, 15, fOut)
                fOut.close()
                capturedImage(it, unitId, printerName, userData, true)
            }
        }) {
            Icon(
                modifier = Modifier
                    .width(45.dp)
                    .height(45.dp),
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Take Photo"
            )
        }
    }

    if (cameraViewModel.status.value != "") {
        LFCAlertDialog(
            Icons.Default.Info,
            "Label free code",
            cameraViewModel.status.value,
            isDialogOpen
        ) {
            isDialogOpen = false
            cameraViewModel.status.value = ""
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
            }
        }
    )
}