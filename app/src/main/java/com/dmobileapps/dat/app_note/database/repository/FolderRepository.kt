package com.dmobileapps.dat.app_note.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dmobileapps.dat.app_note.database.NoteDatabase
import com.dmobileapps.dat.app_note.database.dao.FolderDao
import com.dmobileapps.dat.app_note.model.Folder

class FolderRepository(app: Application) {
    private val folderDao: FolderDao
    init {
        val noteDatabase: NoteDatabase = NoteDatabase.getInstance(app)
        folderDao = noteDatabase.getFolderDao()
    }

    suspend fun insertFolder(folder: Folder) = folderDao.insertFolder(folder)
    suspend fun updateFolder(folder: Folder) = folderDao.updateFolder(folder)
    suspend fun deleteFolder(folder: Folder) = folderDao.deleteFolder(folder)
    suspend fun updateFolderId(id:Int,count:Int) = folderDao.updateFolderbyId(id,count)
    fun getAllFolder(): LiveData<MutableList<Folder>> = folderDao.getAllFolder()
}