package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rfidtab.service.db.entity.kit.KitCommentEntity
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.kitorder.OrderCardEntity
import com.example.rfidtab.service.db.entity.task.CardImagesEntity
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(entity: TaskResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCard(entity: List<TaskCardListEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertImage(entity: CardImagesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOverCard(entity: OverCardsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKitComment(entity: KitCommentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKitItem(entity: KitItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKitRfid(entity: KitRfidEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKitOrder(entity: KitOrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKitOrderCards(list: List<OrderCardEntity>)

    @Query("SELECT * FROM OrderCardEntity WHERE OrderCardEntity.kitInt=:kitId")
    fun findKitOrderCard(kitId: Int): LiveData<List<OrderCardEntity>>

    @Query("SELECT * FROM KitOrderEntity")
    fun findKitOrder(): LiveData<List<KitOrderEntity>>

    @Query("SELECT * FROM TaskResultEntity WHERE TaskResultEntity.userLogin=:userLogin")
    fun findTasksByLogin(userLogin: String): LiveData<List<TaskResultEntity>>

    @Query("SELECT * FROM KitCommentEntity WHERE KitCommentEntity.kitId=:kitId")
    fun findKitComment(kitId: Int): LiveData<List<KitCommentEntity>>

    @Query("SELECT * FROM KitItemEntity WHERE KitItemEntity.userLogin=:userLogin")
    fun findKitItem(userLogin: String): LiveData<List<KitItemEntity>>

    @Query("SELECT * FROM KitRfidEntity WHERE KitRfidEntity.kitId=:kitId")
    fun findKitRfid(kitId: Int): LiveData<List<KitRfidEntity>>

    @Query("SELECT * FROM CardImagesEntity WHERE CardImagesEntity.cardId=:id")
    fun findImagesById(id: Int): LiveData<List<CardImagesEntity>>

    @Query("SELECT * FROM OverCardsEntity WHERE OverCardsEntity.taskId=:id")
    fun findOverCardById(id: Int): LiveData<List<OverCardsEntity>>

    @Query("SELECT * FROM TaskCardListEntity WHERE TaskCardListEntity.taskId=:taskId")
    fun findCardsById(taskId: Int): LiveData<List<TaskCardListEntity>>

    @Query("DELETE FROM TaskResultEntity WHERE TaskResultEntity.id=:id")
    fun deleteTaskById(id: Int)

    @Query("DELETE FROM KitRfidEntity WHERE KitRfidEntity.rfidId=:rfidId")
    fun deleteKitRfid(rfidId : Int)

    @Query("DELETE FROM TaskCardListEntity WHERE TaskCardListEntity.taskId=:id")
    fun deleteCardsById(id: Int)

    @Query("UPDATE TaskCardListEntity SET rfidTagNo=:rfid WHERE cardId =:cardId")
    fun updateCard(cardId: Int, rfid: String)

    @Query("UPDATE TaskCardListEntity SET commentProblemWithMark=:errorComment WHERE cardId =:cardId")
    fun updateErrorComment(cardId: Int, errorComment: String)
}