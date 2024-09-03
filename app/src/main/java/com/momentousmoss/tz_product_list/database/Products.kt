package com.momentousmoss.tz_product_list.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProductsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: ProductsEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateProduct(product: ProductsEntity)

    @Query("SELECT * FROM ProductsEntity")
    fun getProducts(): Cursor

    @Query("SELECT * FROM ProductsEntity WHERE name LIKE '%' || :searchText || '%'")
    fun getProductsBySearch(searchText: String?): Cursor

    @Query("DELETE FROM ProductsEntity WHERE id = :productId")
    fun deleteById(productId: Int)

    @Query("DELETE FROM ProductsEntity")
    fun clear()
}

@Entity
class ProductsEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    var time: Long,
    var tags: String = "[]",
    var amount: Int = 0
)

data class Product (
    var id: Int,
    var name: String,
    var time: Long,
    var tags: String = "",
    var amount: Int = 0
)