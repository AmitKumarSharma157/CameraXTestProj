package com.example.cameraxtestproj

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cameraxtestproj.Utils.Constants
import com.example.cameraxtestproj.ui.CameraPreview
import com.example.cameraxtestproj.ui.CameraScreen
import com.example.cameraxtestproj.viewmodel.CameraViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun InitNavHost(
    navController: NavHostController = rememberNavController()
)
{
    NavHost(navController = navController, startDestination = Constants.Camera_Preview_NAV){
        composable(Constants.Camera_Preview_NAV){
            //CameraPreview(navController)
        }
        composable(Constants.CAMERA_SCREEN_NAV){
            CameraScreen(navController)
        }
    }
}