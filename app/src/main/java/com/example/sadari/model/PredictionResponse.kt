package com.example.sadari.model

data class SubmitResponse(val task_id: String)
data class TaskStatusResponse(val status: String, val result: String?)