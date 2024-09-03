package com.momentousmoss.tz_product_list.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        ProductsEntity::class
    ],
    version = 2
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ProductsDao(): ProductsDao
}