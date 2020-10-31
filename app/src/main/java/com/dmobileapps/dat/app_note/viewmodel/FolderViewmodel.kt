package com.dmobileapps.dat.app_note.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dmobileapps.dat.app_note.database.repository.FolderRepository
import com.dmobileapps.dat.app_note.model.Folder
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FolderViewmodel(application: Application): ViewModel() {
    private val noteRepository: FolderRepository = FolderRepository(application)

    fun insertFolder(note:Folder) = viewModelScope.launch {
        noteRepository.insertFolder(note)
    }

    fun updateFolder(note:Folder) = viewModelScope.launch {
        noteRepository.updateFolder(note)
    }

    fun updateFolderById(id:Int,count:Int) = viewModelScope.launch {
        noteRepository.updateFolderId(id,count)
    }

    fun deleteFolder(note:Folder) = viewModelScope.launch {
        noteRepository.deleteFolder(note)
    }

    fun getAllFolder(): LiveData<MutableList<Folder>> = noteRepository.getAllFolder()

    class NoteViewmodelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FolderViewmodel::class.java)){
                return FolderViewmodel(application) as T
            }
            throw IllegalArgumentException("Unable contructor viewModel")
        }

    }
}