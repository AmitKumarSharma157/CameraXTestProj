package com.example.cameraxtestproj.datawedge

import android.app.Application
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class manages the initialization and operation of the DataWedge scanner in the application.
 * It serves as a central point for handling all DataWedge-related events.
 */
@Singleton
class DataWedgeManager @Inject constructor(private val application: Application) {
    private lateinit var dataWedgeBroadcaster: DataWedgeBroadcaster

    /**
     * Provides a Flow variable that emits scanned barcode data.
     * This can be used for reactive programming, allowing other parts of the app to respond to barcode scans.
     */
    fun scannedCode() = dataWedgeBroadcaster.scannedCodeFlow

    /**
     * Initializes the DataWedge scanner by setting up a broadcast receiver.
     * This method creates a DataWedge profile and registers a receiver to handle scan events.
     */
    fun initializeDataWedge() {
        // Initialize the DataWedgeBroadcaster to manage the scanner.
        dataWedgeBroadcaster = DataWedgeBroadcaster(application.applicationContext)
        dataWedgeBroadcaster.createDataWedgeProfile()

        // Set up an intent filter with the action defined in DataWedgeBroadcaster.
        val intentFilter = IntentFilter()
        intentFilter.addAction(dataWedgeBroadcaster.receiverAction)

        // Register the broadcast receiver to start listening for scanning events.
        ContextCompat.registerReceiver(
            application,
            dataWedgeBroadcaster,
            intentFilter,
            ContextCompat.RECEIVER_EXPORTED
        )
    }

    /**
     * Disables the scanner trigger, preventing it from scanning.
     * This can be used to prevent unwanted scans or manage scanner usage.
     */
    fun blockButtonTrigger() {
        dataWedgeBroadcaster.enableScanner(false)
    }

    /**
     * Re-enables the scanner trigger, allowing it to scan again.
     * This should be called after blockButtonTrigger() when scans are desired again.
     */
    fun unblockButtonTrigger() {
        dataWedgeBroadcaster.enableScanner(true)
    }
}
