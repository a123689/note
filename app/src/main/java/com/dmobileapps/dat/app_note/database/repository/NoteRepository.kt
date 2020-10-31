package com.dmobileapps.dat.app_note.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dmobileapps.dat.app_note.database.NoteDatabase
import com.dmobileapps.dat.app_note.database.dao.NoteDao
import com.dmobileapps.dat.app_note.model.Note

class NoteRepository(app: Application) {
    private val noteDao: NoteDao
    init {
        val noteDatabase: NoteDatabase = NoteDatabase.getInstance(app)
        noteDao = noteDatabase.getNoteDao()
    }

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    suspend fun updateNote(note: Note) = noteDao.updateNote(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    fun getAllNote(id:Int): LiveData<MutableList<Note>> = noteDao.getAllNote(id)
    fun getCountNote(id:Int): LiveData<Int> = noteDao.getCountNote(id)
}