package com.example.rfidtab.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.rfidtab.service.NetworkRepository
import com.example.rfidtab.service.db.AppDatabase
import com.example.rfidtab.service.db.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    protected val network = NetworkRepository()
    protected val db = RoomRepository(AppDatabase.instance(application).dbDao())

    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}