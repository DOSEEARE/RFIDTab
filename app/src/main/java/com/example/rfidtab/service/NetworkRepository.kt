package com.example.rfidtab.service

import androidx.lifecycle.liveData
import com.example.rfidtab.service.db.entity.task.CardImagesEntity
import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.CardModelList
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.confirm.ConfirmCardModel
import com.example.rfidtab.service.model.kit.CreateKitModel
import com.example.rfidtab.service.model.kit.ImageListBase64Model
import com.example.rfidtab.service.model.kitorder.KitOrderModel
import com.example.rfidtab.service.model.overlist.TaskOverCards
import com.example.rfidtab.service.model.search.SearchModel
import com.example.rfidtab.service.response.task.OverCardsResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
        }
    }

    fun sendOverCards(model: TaskOverCards) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().sendOverCards(model)

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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
        }
    }

    fun cardChange(model: CardModel) = liveData(Dispatchers.IO) {

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

    fun cardChangeList(model: CardModelList) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().changeCardList(model)
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
                emit(Resource.netwrok("Произошла ошибка при отправке изображение", null))
            }
        }

    fun sendImageListBase64 (body : ImageListBase64Model) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().sendImageListBase64(body)
            val code = response.code()
            when {
                response.isSuccessful -> {
                    emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Успешно отправлен", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))        }
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
        }
    }

    fun getOverCards(taskId: Int) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().getOverCards(taskId)
            val code = response.code()
            when {
                response.isSuccessful -> { emit(Resource.success(response.body()))
                }
                else -> {
                    emit(Resource.error("Ошибка при загрузке данных", null))
                }
            }
        } catch (e: Exception) {
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
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
            emit(Resource.netwrok("Проблемы с подключением интернета", null))
        }
    }
}

