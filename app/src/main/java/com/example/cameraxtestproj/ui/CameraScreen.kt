package com.example.cameraxtestproj.ui

import AlertDialogExample
import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cameraxtestproj.viewmodel.CameraViewModel
import java.io.File

@Composable
fun CameraScreen(navController: NavController) {
    val context = LocalContext.current
    val path = context.getExternalFilesDir(null)?.absolutePath
    val imagePath = "$path/tempFileName.jpg"
    val image = BitmapFactory.decodeFile(imagePath)
    File(imagePath).deleteOnExit() // Delete temp image

    var isSendImageEnable by remember {
        mutableStateOf(false)
    }

    var openDialog = remember {
        mutableStateOf(true)
    }

    //Log.d("TAG", image.toString())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        image?.let {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(.8f),
                bitmap = it.asImageBitmap(),
                contentDescription = null
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(.2f),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .align(alignment = Alignment.End),
                onClick = {
                    isSendImageEnable = !isSendImageEnable
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue
                )
            ) {
                Text(text = "Send Photo")
            }
        }

        if (isSendImageEnable) {
            val cameraViewModel: CameraViewModel = hiltViewModel()
            cameraViewModel.processImage(image)
            if (cameraViewModel.status.value != "") {
                AlertDialogExample(Icons.Default.Info, "Parse Text", cameraViewModel.status.value, openDialog.value) {
                    openDialog.value = false
                }
            }
        }
    }
}