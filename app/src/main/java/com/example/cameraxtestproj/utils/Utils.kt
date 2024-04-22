package com.example.cameraxtestproj.utils

import android.content.Context

object Utils {

    fun saveQrScannedDataToPreferences(
        context: Context, unitId: String, printerName: String, userData: String
    ) {
        val sharedPreferences = context.getSharedPreferences("QrData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("UnitId", unitId)
            putString("PrinterName", printerName)
            putString("UserData", userData)
            apply()
        }
    }

    fun getUnitIdToPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("QrData", Context.MODE_PRIVATE)
        return sharedPreferences.getString("UnitId", null)
    }

    fun getPrinterNameToPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("QrData", Context.MODE_PRIVATE)
        return sharedPreferences.getString("PrinterName", null)
    }

    fun getUserDataPreferences(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("QrData", Context.MODE_PRIVATE)
        return sharedPreferences.getString("UserData", null)
    }
}
