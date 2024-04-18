package com.example.cameraxtestproj.repository

import com.example.cameraxtestproj.datawedge.DataWedgeManager
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class for handling both DataWedge & ProGlove scanners.
 * Centralized class for getting events and triggering of both scanners.
 */
@Singleton
class ScannersRepository @Inject constructor(
    private val dataWedgeManager: DataWedgeManager
) {

    fun dataWedgeScannedCode() = dataWedgeManager.scannedCode()

    fun initDataWedge() {
        dataWedgeManager.initializeDataWedge()
    }

    fun blockButtonTrigger() {
        dataWedgeManager.blockButtonTrigger()
    }

    fun unblockButtonTrigger() {
        dataWedgeManager.unblockButtonTrigger()
    }
}
