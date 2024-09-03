package com.momentousmoss.tz_product_list.utils

import android.database.Cursor
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.momentousmoss.tz_product_list.database.Product
import com.momentousmoss.tz_product_list.database.ProductsEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)

@TypeConverter
fun convertStringToListString(stringFromList: String?): List<String> {
    return Gson().fromJson(stringFromList, object : TypeToken<List<String?>?>() {}.type)
}

fun dateStringToDateFormat(productTime: Long): String {
    return DATE_FORMAT.format(Date(productTime))
}

fun Product.toProductEntity() = ProductsEntity(
    id = id,
    name = name,
    time = time,
    tags = tags,
    amount = amount
)

fun Cursor.toProduct() = Product(
    id = getInt(0),
    name = getString(1),
    time = getLong(2),
    tags = getString(3),
    amount = getInt(4)
)