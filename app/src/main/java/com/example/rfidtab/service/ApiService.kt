package com.example.rfidtab.service

import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.overlist.TaskOverCards
import com.example.rfidtab.service.response.AuthResponse
import com.example.rfidtab.service.response.task.TaskCardResponse
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.service.response.user.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    @POST("inventory/add-over")
    suspend fun overCards(@Body model: TaskOverCards): Response<String>

    @POST("auth/login")
    suspend fun auth(@Body model: AuthModel): Response<AuthResponse>

    @GET("task/list")
    suspend fun task(@Query("withCards") withCards: Boolean): Response<List<TaskResponse>>

    @GET("user/item")
    suspend fun userInfo(@Query("number") number: Int): Response<UserInfoResponse>

    @POST("card/edit")
    suspend fun changeCard(@Body model: CardModel): Response<TaskCardResponse>

    @POST("task/change-status")
    suspend fun taskStatusChange(@Body model: TaskStatusModel): Response<String>

}