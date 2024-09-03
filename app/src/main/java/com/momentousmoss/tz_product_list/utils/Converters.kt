package com.momentousmoss.tz_product_list.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@TypeConverter
fun convertStringToListString(stringFromList: String?): List<String> {
    return Gson().fromJson(stringFromList, object : TypeToken<List<String?>?>() {}.type)
}

@TypeConverter
fun convertListStringToString(listOfStrings: List<String?>?): String {
    return Gson().toJson(listOfStrings)
}