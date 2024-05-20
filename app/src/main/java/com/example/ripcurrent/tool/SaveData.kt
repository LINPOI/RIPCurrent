package com.example.ripcurrent.tool

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.ripcurrent.tool.Data.Member
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
        else -> Log.i("0123", "Unsupported data type")
    }

    editor.apply()
}


fun readDataClass(context: Context, key: String): Member {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
    val jsonString = sharedPreferences.getString(key, null)

    if (jsonString != null) {
        val gson = Gson()
        val type = object : TypeToken<Member>() {}.type
        return gson.fromJson(jsonString, type)
    }

    return Member()
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