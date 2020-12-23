package com.dmobileapps.dat.app_note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_table")
 class CheckList{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = " id_check")
    var id:Int = 0
    @ColumnInfo(name="text_col")
    var text: String? = ""
    @ColumnInfo(name="text_col")
    var image: String? = ""
 }