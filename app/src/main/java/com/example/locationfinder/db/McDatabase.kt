package com.example.locationfinder.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.locationfinder.LocationFinder
import com.example.locationfinder.constant.McConstants.LOCATION_DB

/**
 * McDatabase
 */
@Database(
    entities = [
        McLocationItemEntity::class,
    ],
    version = 1
)
abstract class McDatabase : RoomDatabase() {
    abstract fun getDao(): McLocationDao

    companion object {
        private var DB_INSTANCE: McDatabase? = null

        fun getDatabase(): McDatabase {
            val tempInstance = DB_INSTANCE
            tempInstance?.let {
                return it
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    LocationFinder.appContext,
                    McDatabase::class.java,
                    LOCATION_DB
                ).build()

                DB_INSTANCE = instance
                return instance
            }
        }
    }
}