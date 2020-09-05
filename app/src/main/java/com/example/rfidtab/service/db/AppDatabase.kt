package com.example.rfidtab.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.rfidtab.service.db.entity.kit.KitCommentEntity
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import com.example.rfidtab.service.db.entity.kitorder.*
import com.example.rfidtab.service.db.entity.task.CardImagesEntity
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity


@Database(
    entities = [
        TaskResultEntity::class,
        TaskCardListEntity::class,
        OverCardsEntity::class,
        CardImagesEntity::class,
        KitCommentEntity::class,
        KitRfidEntity::class,
        KitItemEntity::class,
        KitOrderCardEntity::class,
        KitOrderEntity::class,
        KitOrderKitEntity::class,
        KitOrderSpecificationEntity::class,
        KitOrderAddCardEntity::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dbDao(): RoomDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun instance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "RFIDTab"
                ).build()
                Companion.instance = instance
                instance
            }

        }
    }
}