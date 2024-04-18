package com.example.cameraxtestproj.ui

import LFCAlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cameraxtestproj.Utils.Utils
import com.example.cameraxtestproj.constants.Constants
import com.example.cameraxtestproj.viewmodel.ScannerViewModel

@Composable
fun MainScreenView(
    scannerViewModel: ScannerViewModel, navController: NavController
) {
    var enablePrintLFC by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val unitId: String? = Utils.getUnitIdToPreferences(context)
    var printerName: String? = ""
    var userData: String? = ""
    if (unitId != null) {
        enablePrintLFC = true
        printerName = Utils.getPrinterNameToPreferences(context)
        userData = Utils.getUserDataPreferences(context)
    }

    MainScreen(
        scannerViewModel, navController, enablePrintLFC, unitId, printerName, userData
    ) {
        enablePrintLFC = true
    }
}

@Composable
fun MainScreen(
    scannerViewModel: ScannerViewModel,
    navController: NavController,
    enablePrintLFC: Boolean,
    unitId: String?,
    printerName: String?,
    userData: String?,
    onUpdateProfile: (Boolean) -> Unit
) {
    val openDialog = remember {
        mutableStateOf(true)
    }

    val context = LocalContext.current
    var unitId: String? = unitId
    var printerName: String? = printerName
    var userData: String? = userData

    val lstQrData = scannerViewModel.scannedCode.value?.data?.split("##")
    if (lstQrData != null && lstQrData.count() > 2) {
        unitId = lstQrData[0]
        printerName = lstQrData[1]
        userData = lstQrData[2]
        Utils.saveQrScannedDataToPreferences(context, unitId, printerName, userData)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 20.dp, 20.dp),
            text = "Skriv Label Free Code",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 15.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 18.sp)) {
                        append("Terminal")
                    }
                    append(" - ")
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) {
                        append(unitId)
                    }
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 18.sp)) {
                        append("Printer name")
                    }
                    append(" - ")
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) {
                        append(printerName)
                    }
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 18.sp)) {
                        append("Data")
                    }
                    append(" - ")
                    withStyle(style = SpanStyle(color = Color.Black, fontSize = 16.sp)) {
                        append(userData)
                    }
                })
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            Button(onClick = {
                onUpdateProfile(true)
            }) {
                Text(text = "Update Profile")
            }
            Button(enabled = enablePrintLFC, onClick = {
                navController.navigate(Constants.CAMERA_PREVIEW_NAV)
            }) {
                Text(text = "Print LFC")
            }
        }

        if (enablePrintLFC) {
            LFCAlertDialog(
                Icons.Default.Info,
                "Scanner",
                "Please use device to scan QR code.",
                openDialog.value
            ) {
                openDialog.value = false
            }
        }
    }
}