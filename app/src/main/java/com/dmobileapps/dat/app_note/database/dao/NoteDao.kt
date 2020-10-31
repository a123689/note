package com.dmobileapps.dat.app_note.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dmobileapps.dat.app_note.model.Note

@Dao
interface  NoteDao {

    @Insert
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("select * from note_table where folderId_col=:id")
    fun getAllNote(id:Int): LiveData<MutableList<Note>>

    @Query("select COUNT(*) from note_table where folderId_col=:id")
    fun  getCountNote(id:Int):  LiveData<Int>

}