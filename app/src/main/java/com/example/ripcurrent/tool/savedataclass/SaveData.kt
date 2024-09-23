package com.example.ripcurrent.tool.savedataclass

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.example.ripcurrent.Data.Member
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream

/* save data class*/
fun saveDataClass(context: Context, key: String, data: Any) {//Member(key)
    Log.i("0123","已保存key:$key，data:$data")
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    val gson = Gson()
    val jsonString = gson.toJson(data)
    when(data){
       is Member -> editor.putString(key, jsonString)
        is Int -> editor.putInt(key,data)
        is Boolean -> editor.putBoolean(key, data)
        is String -> editor.putString(key, data)
        is Float -> editor.putFloat(key, data.toFloat())
        is Double -> editor.putFloat(key, data.toDouble().toFloat())
        is List<*> -> {
            if (data.all { it is String }) { // 檢查是否所有項目都是字串
                val stringList = data as List<String>
                val jsonString = gson.toJson(stringList)
                editor.putString(key, jsonString)
            } else {
                Log.i("0123", "Unsupported data type in the list")
            }
        }
        is MutableList<*> -> {
            if (data.all { it is String }) { // 檢查是否所有項目都是字串
                val stringList = data as MutableList<String>
                val jsonString = gson.toJson(stringList)
                editor.putString(key, jsonString)
            } else {
                Log.i("0123", "Unsupported data type in the list")
            }
        }
        is Bitmap -> {
            val outputStream = ByteArrayOutputStream()
            data.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val byteArray = outputStream.toByteArray()
            val encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT)
            editor.putString(key, encodedString)
        }
        else -> Log.i("0123", "Unsupported data type")

    }

    editor.apply()
}


inline fun <reified T> readDataClass(context: Context, key: String): T? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    val jsonString = sharedPreferences.getString(key, null)

    return if (jsonString != null) {
        val gson = Gson()
        val type = object : TypeToken<T>() {}.type
        gson.fromJson(jsonString, type)
    } else {
        null
    }
}
fun readDataClass_Bitmap(context: Context, key: String): Bitmap? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    val encodedString = sharedPreferences.getString(key, null) ?: return null
    val byteArray = Base64.decode(encodedString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}
// read data class
@SuppressLint("SuspiciousIndentation")
fun <T : Any>readDataClass(context: Context, key: String, defaultValue: T): T {
    Log.i("0123","讀取key:$key")
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    @Suppress("UNCHECKED_CAST")
    return when (defaultValue) {
        is String -> sharedPreferences.getString(key, defaultValue as String) as T
        is Int -> sharedPreferences.getInt(key, defaultValue as Int) as T
        is Boolean -> sharedPreferences.getBoolean(key, defaultValue as Boolean) as T
        is Float -> sharedPreferences.getFloat(key, defaultValue as Float) as T
        is Double -> sharedPreferences.getFloat(key, defaultValue as Float).toDouble() as T
        is List<*> -> {
            val json = sharedPreferences.getString(key, null)
            if (json != null) {
                val typeToken = object : TypeToken<List<String>>() {}.type
                Gson().fromJson(json, typeToken) ?: defaultValue
            } else {
                defaultValue
            }
        }
        is MutableList<*> -> {
            val json = sharedPreferences.getString(key, null)
            if (json != null) {
                val typeToken = object : TypeToken<MutableList<String>>() {}.type
                Gson().fromJson(json, typeToken) ?: defaultValue
            } else {
                defaultValue
            }
        }
        // 其他
        else -> throw IllegalArgumentException("Unsupported data type")
    }
}
//Member 會員資料  saveDataClass(hint,"Member",member)
//  readDataClass(hint, "Member", member)?:Member()
//  saveDataClass(hint,"鍵值",變數名)
//  readDataClass(hint, 鍵值", 變數)
//class keyName{
//    val key_member="Member"
//    val key_isSelect="isSelect"
//}