package com.dmobileapps.dat.app_note.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dmobileapps.dat.app_note.database.dao.FolderDao
import com.dmobileapps.dat.app_note.database.dao.NoteDao
import com.dmobileapps.dat.app_note.model.Folder
import com.dmobileapps.dat.app_note.model.Note

@Database(entities = [Folder::class,Note::class],version = 6)
abstract class NoteDatabase : RoomDatabase(){
    abstract fun getFolderDao(): FolderDao
    abstract fun getNoteDao(): NoteDao
    companion object{
        @Volatile
        private var instance:NoteDatabase? = null

        fun getInstance(context: Context):NoteDatabase{
            if(instance == null){
                instance = Room.databaseBuilder(context,NoteDatabase::class.java,"NoteDatabase6").build()
            }
            return instance!!
        }
    }
}