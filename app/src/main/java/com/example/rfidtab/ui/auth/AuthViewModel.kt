package com.example.rfidtab.ui.auth

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.service.response.AuthResponse


class AuthViewModel(application: Application) : BaseViewModel(application) {

    fun auth(authModel: AuthModel): LiveData<Resource<AuthResponse>> {
        return network.auth(authModel)
    }

}