package com.example.cameraxtestproj.Utils

import android.content.Context

object Utils {

    /**
     * Saves the language code and device name to SharedPreferences.
     * @param context The context used for accessing SharedPreferences.
     * @param languageCode The language code to save.
     * @param deviceName The device name to save.
     */
    private fun saveToPreferences(context: Context, languageCode: String, deviceName: String) {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("LanguageCode", languageCode)
            putString("DeviceName", deviceName)
            apply()
        }
    }

    /**
     * Function to get the Printer Name after splitting with ;
     * @return the last selected Printer Name
     */
    fun getPrinterName(value: String): String {
        return if (value.contains("$")) {
            val parts = value.split("$")
            parts[parts.size - 1]
        } else {
            value
        }
    }

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
