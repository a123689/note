package com.dmobileapps.dat.app_note.database

import androidx.room.TypeConverter
import com.dmobileapps.dat.app_note.model.CheckList
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {
//    @TypeConverter
//    fun fromString(value: String?): CheckList {
//        return if (value!=null&&value.isNotEmpty()){
//            Gson().fromJson(value, CheckList::class.java)
//        }else{
//            CheckList()
//        }
//    }
//
//    @TypeConverter
//    fun fromCheckList(checkList: CheckList?): String {
//        return if (checkList!=null){
//
//            Gson().toJson(checkList)
//        }else{
//            ""
//        }
//    }
    @TypeConverter
    fun fromStringArrCheckList(value: String?): ArrayList<CheckList> {
        val listType: Type = object :  TypeToken<ArrayList<CheckList?>?>() {}.type
        return Gson().fromJson(value, listType)
    }


      @TypeConverter
      fun fromArrayRing(list: ArrayList<CheckList?>?): String {
          return Gson().toJson(list)
      }

//      @TypeConverter
//      fun fromStringRing(value: String?): Ring {
//          val listType: Type = object : TypeToken<Ring>() {}.type
//          return Gson().fromJson(value, listType)
//      }
//      @TypeConverter
//      fun fromRing(ring: Ring): String {
//          val gson = Gson()
//          return gson.toJson(ring)
//      }

}