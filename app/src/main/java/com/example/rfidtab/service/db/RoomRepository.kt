package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.response.task.TaskResult

class RoomRepository(private val dao: RoomDao) {

    fun insertTaskToDB(entity: TaskWithCards) {
        dao.insertTask(entity.task)
        dao.insertCard(entity.cards)
    }

    fun updateTasks(data: TaskResult) {

    }

    fun findAllTasks(): LiveData<List<TaskResultEntity>> {
        return dao.findAllTasks()
    }

    fun deleteTaskById(id: Int) {
        return dao.deleteTaskById(id)
    }
}