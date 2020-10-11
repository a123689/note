package com.example.dat.app_note.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.dat.app_note.model.Folder

class FolderRepository(app: Application) {
    private val folderDao:FolderDao
    init {
        val noteDatabase:NoteDatabase = NoteDatabase.getInstance(app)
        folderDao = noteDatabase.getFolderDao()
    }

    suspend fun insertNote(folder: Folder) = folderDao.insertFolder(folder)
    suspend fun updateNote(folder: Folder) = folderDao.updateFolder(folder)
    suspend fun deleteNote(folder: Folder) = folderDao.deleteFolder(folder)

    fun getAllFolder(): LiveData<MutableList<Folder>> = folderDao.getAllFolder()
}