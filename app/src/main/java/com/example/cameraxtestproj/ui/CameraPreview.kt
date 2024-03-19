package com.example.cameraxtestproj.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Size
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.core.UseCaseGroup
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.cameraxtestproj.ImageCaptureRepository
import com.example.cameraxtestproj.InitNavHost
import com.example.cameraxtestproj.R
import com.example.cameraxtestproj.Utils.Constants
import java.io.File
import java.io.FileOutputStream

@Composable
fun CameraPreview(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

//    val imageCapture = ImageCapture.Builder()
//        .setTargetAspectRatio(AspectRatio.RATIO_16_9)
//        .build()
//
//    val preview = Preview.Builder()
//        .setTargetAspectRatio(AspectRatio.RATIO_16_9)
//        .build()
//
//    val useCaseGroup = UseCaseGroup.Builder()
//        .setViewPort(previewView.viewPort!!)
//        .addUseCase(UseCase.snapToSurfaceRotation(0))
//        .addUseCase(preview)
//        .build()

    val controller = remember {
        LifecycleCameraController(context).apply {
            this.setEnabledUseCases(IMAGE_CAPTURE or IMAGE_ANALYSIS)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { it ->
            PreviewView(it).apply {
                this.controller = controller
                controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                controller.imageCaptureTargetSize =
                    CameraController.OutputSize(AspectRatio.RATIO_4_3)
                controller.imageCaptureTargetSize = CameraController.OutputSize(Size(200, 200))
                controller.setZoomRatio(.1f)
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
                it.compress(Bitmap.CompressFormat.JPEG, 30, fOut)
                fOut.close()

                navController.navigate(Constants.CAMERA_SCREEN_NAV)
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