package com.example.cameraxtestproj

import AlertDialogExample
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cameraxtestproj.ui.CameraPreview
import com.example.cameraxtestproj.ui.theme.CameraXTestProjTheme
import com.example.cameraxtestproj.viewmodel.CameraViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, PERMISSIONS_REQUIRED, 0
            )
        }

        setContent {
            val cameraViewModel: CameraViewModel = hiltViewModel()
            var openDialog = remember {
                mutableStateOf(true)
            }
            CameraXTestProjTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //InitNavHost()
                    CameraPreview { image, isOpen ->
                        cameraViewModel.processImage(image)
                        openDialog.value = isOpen
                    }
                    if (cameraViewModel.status.value != "") {
                        AlertDialogExample(
                            Icons.Default.Info,
                            "Label free code",
                            cameraViewModel.status.value,
                            openDialog.value
                        ) {
                            openDialog.value = false
                            cameraViewModel.status.value = ""
                        }
                    }
                }
            }
        }
    }

    private fun hasRequiredCameraPermission(): Boolean {
        return PERMISSIONS_REQUIRED.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private var PERMISSIONS_REQUIRED = arrayOf(Manifest.permission.CAMERA)
    }
}