package com.dmobileapps.dat.app_note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder_table")
class Folder(@ColumnInfo(name = "name_col")
           var name:String = "",

           @ColumnInfo(name="noteCount_col")
           var noteCount:Int = 0){

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "folder_id_col")
    var id:Int = 0

}