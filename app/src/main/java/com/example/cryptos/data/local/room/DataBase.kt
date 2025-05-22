package com.example.cryptos.data.local.room

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.example.cryptos.data.local.room.entities.CoinRoomEntity

@Database(
    entities = [CoinRoomEntity::class],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = DataBase.MyAutoMigration::class
        )
    ],
    version = 2,
    exportSchema = true
)
abstract class DataBase : RoomDatabase() {

    class MyAutoMigration : AutoMigrationSpec

    abstract fun coinsDao(): CoinsDao

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
            ).build()
        }
    }
}