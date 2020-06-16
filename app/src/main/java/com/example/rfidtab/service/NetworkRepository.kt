package com.example.rfidtab.service

import androidx.lifecycle.liveData
import com.timelysoft.kainarcourierapp.service.Resource
import com.example.rfidtab.service.model.AuthModel
import kotlinx.coroutines.Dispatchers


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

}



