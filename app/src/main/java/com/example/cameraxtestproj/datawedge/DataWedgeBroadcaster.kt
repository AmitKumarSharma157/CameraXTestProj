package com.example.cameraxtestproj.datawedge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.cameraxtestproj.constants.ApiConstants
import com.example.cameraxtestproj.model.ScannedCode
import com.example.cameraxtestproj.utils.CommonBarcodeType
import com.example.cameraxtestproj.utils.getCommonBarcodeType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 * BroadcastReceiver subclass for handling DataWedge broadcast events.
 * It listens for scanning events from the DataWedge application and processes the received data.
 */
class DataWedgeBroadcaster(private val context: Context) : BroadcastReceiver() {

    // Coroutine scope for asynchronous operations
    private val dataWedgeScope = CoroutineScope(Dispatchers.Main + Job())

    // Shared flow for broadcasting scanned barcode data
    val scannedCodeFlow = MutableSharedFlow<ScannedCode>()

    // Action name for the receiver, unique to the application package
    val receiverAction: String = "${context.packageName}.RECVR"

    /**
     * Overridden method from BroadcastReceiver to handle incoming intents (scanning events).
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            handleNewIntent(intent)
        } catch (e: Exception) {
            log("Error handling broadcast: ${e.message}")
        }
    }

    /**
     * Processes the incoming Intent and determines the action to be taken.
     * Currently, it handles scanning data received from DataWedge.
     */
    private fun handleNewIntent(intent: Intent?) {
        intent?.let {
            when (it.action) {
                receiverAction -> {
                    handleScannedBarcode(it)
                }
            }
        }
    }

    /**
     * Extracts and processes the barcode data from the received Intent.
     */
    private fun handleScannedBarcode(intent: Intent) {
        val barcodeContent = intent.getStringExtra(ApiConstants.EXTRA_DATAWEDGE_DATA)
        val symbology = intent.getStringExtra(ApiConstants.EXTRA_DATAWEDGE_SYMBOLOGY)

        barcodeContent?.let { s ->
            log("Received Barcode DataWedge: $s")
            notifyOnReceivedBarcode(s, symbology)
        } ?: log("Unrecognized Intent")
    }

    /**
     * Notifies the application components about the received barcode scan.
     * This function emits the barcode and its type through the shared flow.
     */
    private fun notifyOnReceivedBarcode(value: String, symbology: String?) {
        val type = symbology?.getCommonBarcodeType()
        if (type != CommonBarcodeType.Unknown) {
            dataWedgeScope.launch {
                scannedCodeFlow.emit(ScannedCode(value, type?.name))
            }
        } else {
            log("DataWedge Scanned code unknown type - $symbology")
        }
    }

    /**
     * Configures the DataWedge profile for the application.
     * This includes setting up barcode scanning parameters and intent output settings.
     */
    fun createDataWedgeProfile() {
        // Define and configure the DataWedge profile
        val profileName = context.packageName
        // Send intents to DataWedge for profile creation and configuration
        sendDataWedgeIntentWithExtra(
            ApiConstants.ACTION_CREATE_DATAWEDGE_PROFILE,
            profileName
        )

        // Creating the profile with specified name and barcode symbology
        val profileConfig = Bundle().apply {
            putString("PROFILE_NAME", profileName)
            putString("PROFILE_ENABLED", "true")
            putString("CONFIG_MODE", "CREATE_IF_NOT_EXIST")
        }

        val barcodeConfig = Bundle().apply {
            putString("PLUGIN_NAME", "BARCODE")
            putString("RESET_CONFIG", "true")
            putBundle("PARAM_LIST", Bundle().apply {
                putString("configure_all_scanners", "true")
                putString("scanner_input_enabled", "true")
            })
        }
        profileConfig.putBundle("PLUGIN_CONFIG", barcodeConfig)

        val appConfig = Bundle().apply {
            putString("PACKAGE_NAME", profileName) // Associate the profile with this app
            putStringArray("ACTIVITY_LIST", arrayOf("*"))
        }
        profileConfig.putParcelableArray("APP_LIST", arrayOf(appConfig))
        sendDataWedgeIntentWithExtra(
            profileConfig
        )

        // Configuring intent output plugin
        profileConfig.remove("PLUGIN_CONFIG")
        val intentConfig = Bundle().apply {
            putString("PLUGIN_NAME", "INTENT")
            putString("RESET_CONFIG", "true")
            putBundle("PARAM_LIST", Bundle().apply {
                putString("intent_output_enabled", "true")
                putString("intent_action", "${profileName}.RECVR")
                putString("intent_delivery", "2") // Broadcast Intent
            })
        }
        profileConfig.putBundle("PLUGIN_CONFIG", intentConfig)
        sendDataWedgeIntentWithExtra(
            profileConfig
        )

        // Disabling keyboard output
        profileConfig.remove("PLUGIN_CONFIG")
        val keystrokeConfig = Bundle().apply {
            putString("PLUGIN_NAME", "KEYSTROKE")
            putString("RESET_CONFIG", "true")
            putBundle("PARAM_LIST", Bundle().apply {
                putString("keystroke_output_enabled", "false")
            })
        }
        profileConfig.putBundle("PLUGIN_CONFIG", keystrokeConfig)
        sendDataWedgeIntentWithExtra(
            profileConfig
        )
    }


    /**
     * Enables or disables the scanner functionality in DataWedge.
     *
     * @param enabled Pass true to enable the scanner, false to disable.
     */
    fun enableScanner(enabled: Boolean) {
        val action = if (enabled) "RESUME_PLUGIN" else "SUSPEND_PLUGIN"
        sendDataWedgeIntentWithExtra(
            ApiConstants.EXTRA_SCANNER_INPUT_DATAWEDGE_PLUGIN,
            action
        )
    }

    /**
     * A utility method for logging messages, using Timber for enhanced logging capabilities.
     *
     * @param message The message to be logged.
     */
    private fun log(message: String) {
        //Timber.tag("SortApp.DataWedge").d(message)
    }

    /**
     * Sends a broadcast intent with an extra string data.
     *
     * @param extraKey The key for the extra data.
     * @param extraValue The value for the extra data.
     */
    private fun sendDataWedgeIntentWithExtra(extraKey: String, extraValue: String) {
        val dataWedgeIntent = Intent().apply {
            action = ApiConstants.ACTION_CONFIG_DATAWEDGE_SEND
            putExtra(extraKey, extraValue)
        }
        context.sendBroadcast(dataWedgeIntent)
    }

    /**
     * Sends a broadcast intent with an extra Bundle.
     *
     * @param extras The Bundle containing additional data.
     */
    private fun sendDataWedgeIntentWithExtra(extras: Bundle) {
        val dataWedgeIntent = Intent().apply {
            action = ApiConstants.ACTION_CONFIG_DATAWEDGE_SEND
            putExtra(ApiConstants.ACTION_CONFIG_DATAWEDGE_PROFILE, extras)
        }
        context.sendBroadcast(dataWedgeIntent)
    }
}
