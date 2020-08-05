package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rfidtab.service.db.entity.task.CardImagesEntity
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity

@Dao
interface RoomDao {

/*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTaskToDB(entity: Task)
*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(entity: TaskResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(entity: List<TaskCardListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(entity: CardImagesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOverCard(entity: OverCardsEntity)

    @Query("SELECT * FROM TaskResultEntity WHERE TaskResultEntity.userLogin=:userLogin")
    fun finTasksByLogin(userLogin: String): LiveData<List<TaskResultEntity>>

    @Query("SELECT * FROM CardImagesEntity WHERE CardImagesEntity.cardId=:id")
    fun findImagesById(id: Int): LiveData<List<CardImagesEntity>>

    @Query("SELECT * FROM OverCardsEntity WHERE OverCardsEntity.taskId=:id")
    fun findOverCardById(id: Int): LiveData<List<OverCardsEntity>>

    @Query("SELECT * FROM TaskCardListEntity WHERE TaskCardListEntity.taskId=:taskId")
    fun findCardsById(taskId: Int): LiveData<List<TaskCardListEntity>>

/*    @Query("DELETE FROM TaskResultEntity")
    fun deleteAllTasks(): LiveData<List<TaskResultEntity>>*/

    @Query("DELETE FROM TaskResultEntity WHERE TaskResultEntity.id=:id")
    fun deleteTaskById(id: Int)

    @Query("DELETE FROM TaskCardListEntity WHERE TaskCardListEntity.taskId=:id")
    fun deleteCardsById(id: Int)

    @Query("UPDATE TaskCardListEntity SET rfidTagNo=:rfid WHERE cardId =:cardId")
    fun updateCard(cardId: Int, rfid: String)

    @Query("UPDATE TaskCardListEntity SET commentProblemWithMark=:errorComment WHERE cardId =:cardId")
    fun updateErrorComment(cardId: Int, errorComment: String)
}