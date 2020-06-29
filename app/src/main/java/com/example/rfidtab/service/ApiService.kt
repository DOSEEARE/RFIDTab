package com.example.rfidtab.service

import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.response.AuthResponse
import com.example.rfidtab.service.response.task.TaskResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @POST("auth/login")
    suspend fun auth(@Body model: AuthModel): Response<AuthResponse>

    @GET("task/list")
    suspend fun task(@Query("withCards") withCards: Boolean): Response<TaskResponse>

}