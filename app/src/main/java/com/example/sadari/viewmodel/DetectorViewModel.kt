package com.example.sadari.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.sadari.data.UserRepository
import kotlinx.coroutines.delay
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class DetectorViewModel : ViewModel() {
        private val repository = UserRepository()

        private val _result = MutableStateFlow<String?>(null)
        val result = _result.asStateFlow()

        private val _loading = MutableStateFlow(false)
        val loading = _loading.asStateFlow()

        fun submitAndPollShape(file: MultipartBody.Part) {
            viewModelScope.launch {
                _loading.value = true
                try {
                    val submitResponse = repository.submitImage(file)
                    val taskId = submitResponse.body()?.task_id ?: return@launch

                    while (true) {
                        delay(5000)
                        val resultResponse = repository.getResult(taskId)
                        val body = resultResponse.body()

                        when (body?.status) {
                            "done" -> {
                                _result.value = body.result
                                return@launch
                            }
                            "error" -> {
                                _result.value = "Gagal: ${body.result}"
                                return@launch
                            }
                            else -> {
                                Log.d("Polling", "Masih menunggu hasil analisis...")
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _result.value = "Terjadi kesalahan: ${e.message}"
                } finally {
                    _loading.value = false
                }
            }
        }

    fun reset() {
        _result.value = null
        _loading.value = false
    }


}