package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import org.jetbrains.annotations.NotNull

@Dao
interface RoomDao {

/*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTaskToDB(entity: Task)
*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(entity: TaskResultEntity)

    @NotNull
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(entity: List<TaskCardListEntity>)

    @Query("SELECT * FROM TaskResultEntity")
    fun findAllTasks(): LiveData<List<TaskResultEntity>>

    @Query("SELECT * FROM TaskCardListEntity WHERE TaskCardListEntity.taskId=:taskId")
    fun findCardsById(taskId: Int): LiveData<List<TaskCardListEntity>>

/*    @Query("DELETE FROM TaskResultEntity")
    fun deleteAllTasks(): LiveData<List<TaskResultEntity>>*/

    @Query("DELETE FROM TaskResultEntity WHERE TaskResultEntity.id=:id")
    fun deleteTaskById(id: Int)

    @Query("UPDATE TaskCardListEntity SET rfidTagNo=:rfid WHERE cardId =:cardId")
    fun updateCard(cardId: Int, rfid: Long)
}