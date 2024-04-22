package com.example.cameraxtestproj.remote
import com.example.cameraxtestproj.model.ImageProcessingRequest
import com.example.cameraxtestproj.model.ImageProcessingResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Retrofit interface that have the http requests, each request method will return [Response]
 * of expected data structure.
 */
interface RemoteService {

//    @GET("")
//    suspend fun fetchUserProfile(
//        @Query("userId") userId: String,
//        @Query("CountryCode") countryCode: String
//    ): Response<UserSortingProfile>

    @POST("ImageProcessing/ProcessImage")
    suspend fun sendImage(
        @Body request: ImageProcessingRequest
    ): Response<ImageProcessingResult>
}

