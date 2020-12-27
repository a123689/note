package com.dmobileapps.dat.app_note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class CheckList(
    var id:Int = 0,
    var title: String = "",
    var images: ArrayList<String> ?=null,
    var audios: ArrayList<String> ?=null,
)