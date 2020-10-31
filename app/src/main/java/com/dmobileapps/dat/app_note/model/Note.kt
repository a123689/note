package com.dmobileapps.dat.app_note.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
 class Note(@ColumnInfo(name = "content_col")
            var content: String? = "",

            @ColumnInfo(name="date_col")
            var date: String? = "",

            @ColumnInfo(name="folderId_col")
            var folderId:Int = 0,

           var isCheck:Boolean = false) : Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id_col")
    var id:Int = 0

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(content)
        parcel.writeString(date)
        parcel.writeInt(folderId)
        parcel.writeByte(if (isCheck) 1 else 0)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }


}