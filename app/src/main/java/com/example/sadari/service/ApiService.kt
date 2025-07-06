package com.example.sadari.service

import com.example.sadari.model.SubmitResponse
import com.example.sadari.model.TaskStatusResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("submit")
    suspend fun submitImage(@Part file: MultipartBody.Part): Response<SubmitResponse>

    @GET("result/{task_id}")
    suspend fun getResult(@Path("task_id") taskId: String): Response<TaskStatusResponse>
}