package com.example.rfidtab.service

import androidx.lifecycle.liveData
import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.overlist.TaskOverCards
import kotlinx.coroutines.Dispatchers
import okhttp3.MultipartBody


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
            emit(Resource.netwrok("Проблеммы с подключение интернета", null))
        }
    }

    fun sendImage(image: MultipartBody.Part, cardId: Int) = liveData(Dispatchers.IO) {
        try {
            val response = RetrofitClient.apiService().sendImage(image, cardId)
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

    fun task(withCards : Boolean) = liveData(Dispatchers.IO) {
        try {

            val response = RetrofitClient.apiService().task(withCards)
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

    fun userInfo(number : Int) = liveData(Dispatchers.IO) {
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
}

