package com.example.rfidtab.ui.task

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.rfidtab.base.BaseViewModel
import com.example.rfidtab.service.Resource
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.response.task.TaskResponse

class TasksViewModel(application: Application) : BaseViewModel(application) {

    fun taskNetwork(withCards: Boolean): LiveData<Resource<TaskResponse>> {
        return network.task(withCards)
    }

    fun insertTaskToDb(item: TaskWithCards) {
        db.insertTaskToDB(item)
    }

    fun findAllTasks(): LiveData<List<TaskResultEntity>> {
        return db.findAllTasks()
    }

    fun deleteTaskById(id: Int) {
        return db.deleteTaskById(id)
    }
}