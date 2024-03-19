package com.example.cameraxtestproj.repository

import BaseDataSource
import com.example.cameraxtestproj.model.ImageProcessingRequest
import com.example.cameraxtestproj.model.ImageProcessingResult
import com.postenbring.sortapp.services.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import javax.inject.Inject

class ImageRepository @Inject constructor(private val remoteDataSource: RemoteDataSource){
    suspend fun sendImage(
        request: ImageProcessingRequest
    ): Flow<Response<ImageProcessingResult>> {
        return flow {
            emit(
                remoteDataSource.sendImage(request)
                )
        }.flowOn(Dispatchers.IO)
    }
}