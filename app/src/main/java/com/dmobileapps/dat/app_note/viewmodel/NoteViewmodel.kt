package com.dmobileapps.dat.app_note.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dmobileapps.dat.app_note.database.repository.NoteRepository
import com.dmobileapps.dat.app_note.model.Note
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class NoteViewmodel(application: Application): ViewModel() {
    private val noteRepository: NoteRepository = NoteRepository(application)

    fun insertNote(note: Note) = viewModelScope.launch {
        noteRepository.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        noteRepository.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        noteRepository.deleteNote(note)
    }

    fun getAllNote(id:Int): LiveData<MutableList<Note>> = noteRepository.getAllNote(id)
    fun getCountNote(id:Int): LiveData<Int> = noteRepository.getCountNote(id)

    class NoteViewmodelFactory(private val application: Application) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NoteViewmodel::class.java)){
                return NoteViewmodel(application) as T
            }
            throw IllegalArgumentException("Unable contructor viewModel")
        }

    }
}