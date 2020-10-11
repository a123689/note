package com.example.dat.app_note.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dat.app_note.model.Folder

@Dao
interface  FolderDao {

    @Insert
    suspend fun insertFolder(note: Folder)

    @Update
    suspend fun updateFolder(note: Folder)

    @Delete
    suspend fun deleteFolder(note: Folder)

    @Query("select * from folder_table")
    fun getAllFolder(): LiveData<MutableList<Folder>>


}