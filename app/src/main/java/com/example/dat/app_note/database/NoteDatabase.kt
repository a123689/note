package com.example.dat.app_note.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.dat.app_note.model.Folder

@Database(entities = [Folder::class],version = 1)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun getNoteDao():FolderDao

    companion object{
        @Volatile
        private var instance:NoteDatabase? = null

        fun getInstance(context: Context):NoteDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context,NoteDatabase::class.java,"NoteDatabase").build()
            }
            return instance!!
        }
    }
}