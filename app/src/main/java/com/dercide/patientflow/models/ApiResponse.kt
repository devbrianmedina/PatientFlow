package com.dercide.patientflow.models

data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: List<Any>
)