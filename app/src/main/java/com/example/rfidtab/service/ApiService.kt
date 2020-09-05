package com.example.rfidtab.service

import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.confirm.ConfirmCardModel
import com.example.rfidtab.service.model.kit.CreateKitModel
import com.example.rfidtab.service.model.kitorder.KitOrderModel
import com.example.rfidtab.service.model.overlist.TaskOverCards
import com.example.rfidtab.service.model.search.SearchModel
import com.example.rfidtab.service.response.AuthResponse
import com.example.rfidtab.service.response.SearchResponse
import com.example.rfidtab.service.response.kitorder.KitOrderResponse
import com.example.rfidtab.service.response.task.TaskCardResponse
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.service.response.user.UserInfoResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun auth(@Body model: AuthModel): Response<AuthResponse>

    @POST("inventory/add-over")
    suspend fun overCards(@Body model: TaskOverCards): Response<String>

    @Headers("Content-Type: multipart/form-data")
    @Multipart
    @POST("card/file/{id}")
    suspend fun sendImage(
        @Part image: MultipartBody.Part,
        @Path("id") cardId: Int
    ): Response<String>

    @GET("task/list")
    suspend fun task(@Query("withCards") withCards: Boolean): Response<List<TaskResponse>>

    @GET("equipment/item")
    suspend fun kitOrder(@Query("id") taskId: Int): Response<KitOrderResponse>

    @GET("user/item")
    suspend fun userInfo(@Query("number") number: Int): Response<UserInfoResponse>

    @POST("card/identification")
    suspend fun searchCard(@Body model: SearchModel): Response<SearchResponse>

    @POST("card/edit")
    suspend fun changeCard(@Body model: CardModel): Response<TaskCardResponse>

    @POST("task/change-status")
    suspend fun taskStatusChange(@Body model: TaskStatusModel): Response<String>

    @POST("kit/create")
    suspend fun createKit(@Body model: CreateKitModel): Response<String>

    @POST("equipment/add-card/list")
    suspend fun sendKitOrderCards(@Body model: KitOrderModel): Response<String>

    @POST("equipment/confirmation")
    suspend fun confirmCards(@Body model: ConfirmCardModel): Response<String>

}