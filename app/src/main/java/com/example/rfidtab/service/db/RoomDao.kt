package com.example.rfidtab.service.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.rfidtab.service.db.entity.kit.KitCommentEntity
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import com.example.rfidtab.service.db.entity.kit.ProblemCardEntity
import com.example.rfidtab.service.db.entity.kitorder.*
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
    fun insertKitItem(entity: List<KitOrderKitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAddCard(entity: KitOrderAddCardEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProblemCard (entity: ProblemCardEntity)

    @Query("SELECT * FROM ProblemCardEntity WHERE ProblemCardEntity.cardId=:cardId AND taskId=:taskId")
    fun findProblemCard (cardId : Int, taskId : Int): LiveData<ProblemCardEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKitOrderCards(list: List<KitOrderCardEntity>)

    @Query("SELECT * FROM KitOrderAddCardEntity WHERE KitOrderAddCardEntity.kitId=:kitId")
    fun findAddCardsByKitId(kitId: Int): LiveData<List<KitOrderAddCardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertKitOrderSpec(entity: KitOrderSpecificationEntity)

    @Query("SELECT * FROM KitOrderKitEntity WHERE KitOrderKitEntity.taskId=:taskId")
    fun findKitItem(taskId: Int): LiveData<List<KitOrderKitEntity>>

    @Query("SELECT * FROM KitOrderSpecificationEntity WHERE KitOrderSpecificationEntity.kitId=:kitId")
    fun findKitOrderSpecByKitId(kitId: Int): LiveData<KitOrderSpecificationEntity>

    @Query("SELECT * FROM KitOrderCardEntity WHERE KitOrderCardEntity.kitId=:kitId")
    fun findKitOrderCard(kitId: Int): LiveData<List<KitOrderCardEntity>>

    @Query("SELECT * FROM KitOrderCardEntity WHERE KitOrderCardEntity.kitId=:kitId")
    fun findKitOrderProblemCard (kitId: Int): LiveData<List<KitOrderCardEntity>>

    @Query("SELECT * FROM KitOrderEntity WHERE KitOrderEntity.userLogin=:userLogin")
    fun findKitOrderByLogin(userLogin: String): LiveData<List<KitOrderEntity>>

    @Query("SELECT * FROM TaskResultEntity WHERE TaskResultEntity.userLogin=:userLogin")
    fun findTasksByLogin(userLogin: String): LiveData<List<TaskResultEntity>>

    @Query("SELECT * FROM KitCommentEntity WHERE KitCommentEntity.kitId=:kitId")
    fun findKitComment(kitId: Int): LiveData<List<KitCommentEntity>>

    @Query("SELECT * FROM KitItemEntity WHERE KitItemEntity.userLogin=:userLogin")
    fun findKitItem(userLogin: String): LiveData<List<KitItemEntity>>

    @Query("SELECT * FROM KitItemEntity WHERE KitItemEntity.userLogin=:userLogin")
    suspend fun findKitItemNoLive (userLogin: String): List<KitItemEntity>

    @Query("SELECT * FROM KitRfidEntity WHERE KitRfidEntity.kitId=:kitId")
    fun findKitRfid(kitId: Int): LiveData<List<KitRfidEntity>>

    @Query("SELECT * FROM CardImagesEntity WHERE CardImagesEntity.cardId=:cardId AND CardImagesEntity.taskId=:taskId")
    fun findImagesById(cardId: Int, taskId: Int): LiveData<List<CardImagesEntity>>

    @Query("SELECT * FROM OverCardsEntity WHERE OverCardsEntity.taskId=:id")
    fun findOverCardById(id: Int): LiveData<List<OverCardsEntity>>

    @Query("SELECT * FROM OverCardsEntity WHERE OverCardsEntity.taskId=:id")
   suspend fun findOverCardByIdNoLIve (id: Int): List<OverCardsEntity>

    @Query("SELECT * FROM TaskCardListEntity WHERE TaskCardListEntity.taskId=:taskId")
    fun findCardsById(taskId: Int): LiveData<List<TaskCardListEntity>>

    @Query("DELETE FROM TaskResultEntity WHERE TaskResultEntity.id=:id")
    fun deleteTaskById(id: Int)

    @Query("DELETE FROM CardImagesEntity WHERE CardImagesEntity.cardId=:cardId")
    fun deleteCardImageById(cardId: Int)

    @Query("DELETE FROM OverCardsEntity WHERE OverCardsEntity.taskId=:taskId")
    fun deleteOverCards(taskId: Int)

    @Query("DELETE FROM KitOrderEntity WHERE KitOrderEntity.id=:id")
    fun deleteKitTaskById(id: Int)

    @Query("DELETE FROM KitRfidEntity WHERE KitRfidEntity.rfidId=:rfidId")
    fun deleteKitRfid(rfidId: Int)

    @Query("DELETE FROM KitRfidEntity WHERE KitRfidEntity.kitId=:kitId")
    fun deleteKitAllRfid(kitId: Int)

    @Query("DELETE FROM KitItemEntity WHERE KitItemEntity.kitId=:kitId")
    fun deleteKitItem(kitId: Int)

    @Query("DELETE FROM TaskCardListEntity WHERE TaskCardListEntity.taskId=:id")
    fun deleteCardsById(id: Int)

    @Query("UPDATE TaskCardListEntity SET rfidTagNo=:rfid WHERE cardId =:cardId")
    fun updateCard(cardId: Int, rfid: String)

    @Query("UPDATE TaskCardListEntity SET isConfirmed=:isConfirmed WHERE cardId =:cardId")
    fun updateCardConfirm(cardId: Int, isConfirmed: Boolean)

    @Query("UPDATE TaskCardListEntity SET comment=:comment WHERE cardId =:cardId")
    fun updateCardComment (cardId: Int, comment : String)

    @Query("UPDATE KitOrderCardEntity SET isConfirmed=:isConfirmed WHERE id =:cardId")
    fun kitOrderCardConfirm(cardId: Int, isConfirmed: Boolean)

    @Query("UPDATE KitOrderCardEntity SET rfidTagNo=:rfid WHERE id =:cardId")
    fun updateKitCard(cardId: Int, rfid: String)

    @Query("UPDATE KitOrderCardEntity SET problemComment=:problemComment WHERE id =:cardId")
    fun updateProblemCommentKitCard(cardId: Int, problemComment: String)

    @Query("UPDATE TaskCardListEntity SET commentProblemWithMark=:errorComment WHERE cardId =:cardId")
    fun updateErrorComment(cardId: Int, errorComment: String)

    @Query("UPDATE KitOrderCardEntity SET comment=:errorComment WHERE id =:cardId AND kitId=:kitId")
    fun updateKitErrorComment(cardId: Int, kitId: Int, errorComment: String)

    @Query("UPDATE TaskCardListEntity SET isConfirmed=:isConfirmed WHERE cardId =:cardId")
    fun updateConfirmTaskCard(cardId: Int, isConfirmed: Boolean)

    @Query("SELECT COUNT(isConfirmed) FROM TaskCardListEntity WHERE isConfirmed = 1 AND taskId = :taskId")
    fun getConfirmedCardsCount(taskId: Int):LiveData <Int>

    @Query("SELECT COUNT(isConfirmed) FROM TaskCardListEntity WHERE isConfirmed = 0 AND taskId = :taskId")
    fun getUnConfirmedCardsCount(taskId: Int): LiveData <Int>
}