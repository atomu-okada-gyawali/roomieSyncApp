package com.example.roomiesync.model
import android.os.Parcel
import android.os.Parcelable

data class ChoreModel(
    var choreId: String = "",
    var choreName: String = "",
    var date: Long = System.currentTimeMillis(),
    var userName: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readLong(), // Corrected to read Long
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(choreId)
        parcel.writeString(choreName)
        parcel.writeLong(date) // Corrected to write Long
        parcel.writeString(userName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChoreModel> {
        override fun createFromParcel(parcel: Parcel): ChoreModel {
            return ChoreModel(parcel)
        }

        override fun newArray(size: Int): Array<ChoreModel?> {
            return arrayOfNulls(size)
        }
    }
}
