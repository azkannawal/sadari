package com.example.sadari.data

import com.example.sadari.model.SubmitResponse
import com.example.sadari.model.TaskStatusResponse
import com.example.sadari.service.RetrofitInstance
import okhttp3.MultipartBody
import retrofit2.Response

class UserRepository {
    suspend fun submitImage(file: MultipartBody.Part): Response<SubmitResponse> {
        return RetrofitInstance.api.submitImage(file)
    }

    suspend fun getResult(taskId: String): Response<TaskStatusResponse> {
        return RetrofitInstance.api.getResult(taskId)
    }
}