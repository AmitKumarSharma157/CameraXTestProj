package com.postenbring.sortapp.services.remote

import com.example.cameraxtestproj.model.ImageProcessingRequest
import com.example.cameraxtestproj.remote.RemoteService
import javax.inject.Inject

/**
 * Class that acts as a source for the network layer, which other layers and components can interact
 * with. It has the methods needed to perform the network requests, each method will return
 * remote [retrofit2.Response] with proper body type, and possible error type.
 *
 * @param remoteService interface that have the Retrofit requests.
 */
class RemoteDataSource @Inject constructor(private val remoteService: RemoteService) {
    suspend fun sendImage(request: ImageProcessingRequest) =
        remoteService.sendImage(request)
}
