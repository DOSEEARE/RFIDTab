package com.example.rfidtab.ui.auth

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.rfidtab.service.NetworkRepository
import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.response.AuthResponse
import com.example.rfidtab.service.Resource


class AuthViewModel(application: Application) : ViewModel() {

    private val repository = NetworkRepository()

    fun auth(authModel: AuthModel): LiveData<Resource<AuthResponse>> {
        return repository.auth(authModel)
    }

}