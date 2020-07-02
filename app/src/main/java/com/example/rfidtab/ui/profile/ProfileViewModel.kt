package com.example.rfidtab.ui.profile

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.response.user.UserInfoResponse

class ProfileViewModel(application: Application) : BaseViewModel(application) {

    fun userInfoModel(number: Int): LiveData<Resource<UserInfoResponse>> {
        return network.userInfo(number)
    }
}