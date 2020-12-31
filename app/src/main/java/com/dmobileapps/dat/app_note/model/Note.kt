package com.dmobileapps.dat.app_note.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dmobileapps.dat.app_note.database.Converters

@Entity(tableName = "note_table")
class Note(
    @ColumnInfo(name = "content_col")
    var content: String? = "",
    @ColumnInfo(name = "date_col")
    var date: String? = "",
    @ColumnInfo(name = "avatar")
    var avatar: String? = "",
    @ColumnInfo(name = "folderId_col")
        var folderId: Int = 0,
    var isCheck: Boolean = false,
    @ColumnInfo(name = "date_check_list")
    @TypeConverters(Converters::class)
    var checkList: ArrayList<CheckList> = ArrayList()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id_col")
    var id: Int = 0


    //    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readInt(),
//        parcel.readInt(),
//        parcel.readByte() != 0.toByte()
//    ) {
//        id = parcel.readInt()
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(content)
//        parcel.writeString(date)
//        parcel.writeInt(folderId)
//        parcel.writeByte(if (isCheck) 1 else 0)
//        parcel.writeInt(id)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Note> {
//        override fun createFromParcel(parcel: Parcel): Note {
//            return Note(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Note?> {
//            return arrayOfNulls(size)
//        }
//    }


}