package com.dmobileapps.dat.app_note.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmobileapps.dat.app_note.model.Folder

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

    @Query("Update folder_table set noteCount_col=:count  where folder_id_col=:id")
    suspend fun updateFolderbyId(id:Int,count:Int)



}