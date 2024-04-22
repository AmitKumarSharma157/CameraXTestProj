package com.example.cameraxtestproj

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cameraxtestproj.constants.Constants
import com.example.cameraxtestproj.ui.CameraPreviewScreen
import com.example.cameraxtestproj.ui.MainScreenView
import com.example.cameraxtestproj.viewmodel.CameraViewModel
import com.example.cameraxtestproj.viewmodel.ScannerViewModel

@Composable
fun InitNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Constants.MAIN_SCREEN_NAV) {
        composable(Constants.MAIN_SCREEN_NAV) {
            val scannerViewModel: ScannerViewModel = hiltViewModel()
            MainScreenView(scannerViewModel, navController)
        }
        composable(Constants.CAMERA_PREVIEW_NAV) {
            val cameraViewModel: CameraViewModel = hiltViewModel()
            CameraPreviewScreen(cameraViewModel)
        }
    }
}