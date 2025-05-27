package com.example.cryptos.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cryptos.data.local.room.entities.CoinRoomEntity
import com.example.cryptos.data.local.room.entities.UserProfileEntity

@Database(
    entities = [CoinRoomEntity::class, UserProfileEntity::class],
//    autoMigrations = [
//        AutoMigration(
//            from = 1,
//            to = 2,
//            spec = DataBase.MyAutoMigration::class
//        )
//    ],
    version = 1,
    exportSchema = true
)
abstract class DataBase : RoomDatabase() {

//    class MyAutoMigration : AutoMigrationSpec

    abstract fun coinsDao(): CoinsDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        private const val DATABASE_NAME = "coins.db"

        @Volatile
        private var INSTANCE: DataBase? = null

        fun getDataBase(context: Context): DataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDataBase(context).also { dataBase ->
                    INSTANCE = dataBase
                }
            }
        }

        private fun buildDataBase(context: Context): DataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                DataBase::class.java,
                DATABASE_NAME
            )
//                .fallbackToDestructiveMigration(true)
                .build()
        }
    }
}