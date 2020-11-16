package com.example.rfidtab.service

import android.util.Log
import androidx.lifecycle.liveData
import com.example.rfidtab.service.db.entity.task.CardImagesEntity
import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.confirm.ConfirmCardModel
import com.example.rfidtab.service.model.kit.CreateKitModel
import com.example.rfidtab.service.model.kitorder.KitOrderModel
import com.example.rfidtab.service.model.overlist.TaskOverCards
import com.example.rfidtab.service.model.search.SearchModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class NetworkRepository {

    fun auth(model: AuthModel) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().auth(model)
            val code = response.code()
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверный логин или пароль", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun overCards(model: TaskOverCards) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().overCards(model)

            val code = response.code()
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверный логин или пароль", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun cardChange(model: CardModel) = liveData(Dispatchers.IO) {
        Log.e("SUKABLYA", Gson().toJson(model))

        try {
            val response = RetrofitClient.apiService().changeCard(model)
            val code = response.code()
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверно заполнен!", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблемы с подключение интернета", null))
        }
    }

/*    fun sendImage(image: MultipartBody.Part, cardId: Int) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().sendImage(image, cardId)
            val code = response.code()
            when {
                response.isSuccessful -> { emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверно заполнен!", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }*/

    fun sendImage(imagesPath: List<CardImagesEntity>, cardId: Int, taskTypeId: Int, taskId: Int) =
        liveData(Dispatchers.IO) {
            try {
                imagesPath.forEachIndexed { index, it ->
                    if (File(it.imagePath).exists()) {
                        val file = File(it.imagePath)

                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        val image =
                            MultipartBody.Part.createFormData("image", file.name, requestFile)

                        val response =
                            RetrofitClient.apiService().sendImage(image, taskId, taskTypeId, cardId)
                        val code = response.code()
                        when {
                            response.isSuccessful -> {
                                if (index == imagesPath.size -1)
                                emit(Resource.success(response.body()))
                            }
                            else -> {
                                emit(Resource.error("Ошибка ${response.message()}!", null))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                emit(Resource.netwrok("Произошла ошибка при отправке изоброжение", null))
            }
        }

    fun taskStatusChange(model: TaskStatusModel) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().taskStatusChange(model)
            val code = response.code()
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверно заполнен!", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun createKit (model: CreateKitModel) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().createKit(model)
            val code = response.code()
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверно заполнен!", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun sendKitOrderCards (model: KitOrderModel) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().sendKitOrderCards(model)
            val code = response.code()
            when {
                response.isSuccessful -> { emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверно заполнен!", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))        }
    }

    fun confirmCards (model: ConfirmCardModel) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().confirmCards(model)
            val code = response.code()
            when {
                response.isSuccessful -> { emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Неверно заполнен!", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun task(withCards : Boolean) = liveData(Dispatchers.IO) {
        try {

            val response = RetrofitClient.apiService().task(withCards)
            when {
                response.isSuccessful -> { emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Ошибка при загрузке данных", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun kitOrder(taskId: Int) = liveData(Dispatchers.IO) {
        try {

            val response = RetrofitClient.apiService().kitOrder(taskId)
            val code = response.code()
            when {
                response.isSuccessful -> { emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Ошибка при загрузке данных", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun userInfo(number: Int) = liveData(Dispatchers.IO) {
        try {

            val response = RetrofitClient.apiService().userInfo(number)
            val code = response.code()
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Ошибка при загрузке данных", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun searchCard(model: SearchModel) = liveData(Dispatchers.IO) {
        try {

            val response = RetrofitClient.apiService().searchCard(model)
            val code = response.code()
            when {
                response.isSuccessful -> { emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Не найдено!", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }
}

