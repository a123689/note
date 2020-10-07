package com.example.dat.app_note.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dat.app_note.database.FolderRepository
import com.example.dat.app_note.model.Folder
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class FolderViewmodel(application: Application): ViewModel() {
    private val noteRepository:FolderRepository = FolderRepository(application)

    fun insertFolder(note:Folder) = viewModelScope.launch {
        noteRepository.insertNote(note)
    }

    fun updateFolder(note:Folder) = viewModelScope.launch {
        noteRepository.updateNote(note)
    }

    fun deleteFolder(note:Folder) = viewModelScope.launch {
        noteRepository.deleteNote(note)
    }

    fun getAllFolder(): LiveData<List<Folder>> = noteRepository.getAllNote()


    class NoteViewmodelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FolderViewmodel::class.java)){
                return FolderViewmodel(application) as T
            }
            throw IllegalArgumentException("Unable contructor viewModel")
        }

    }
}