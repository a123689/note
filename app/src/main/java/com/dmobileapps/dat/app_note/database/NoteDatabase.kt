package com.dmobileapps.dat.app_note.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dmobileapps.dat.app_note.database.dao.FolderDao
import com.dmobileapps.dat.app_note.database.dao.NoteDao
import com.dmobileapps.dat.app_note.model.Folder
import com.dmobileapps.dat.app_note.model.Note

@Database(entities = [Folder::class, Note::class], version = 9)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun getFolderDao(): FolderDao
    abstract fun getNoteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null


        private val MIGRATION = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.apply {
                    execSQL("ALTER TABLE widgets ADD COLUMN avatar String DEFAULT ''")
                    execSQL("ALTER TABLE widgets ADD COLUMN checkList String DEFAULT ''")
                }
            }
        }
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance =  Room.databaseBuilder(context, NoteDatabase::class.java, "NoteDatabase6")
                        .addMigrations(MIGRATION)
                        .build()
            }
            return instance!!
        }
    }

}