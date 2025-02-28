package com.example.roomiesync.model
import android.os.Parcel
import android.os.Parcelable

data class ExpenseModel(
    var expenseId: String = "",
    var expenseName: String = "",
    var expenseAmt: Double = 0.0,
    var userName: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",  // expenseId
        parcel.readString() ?: "",  // expenseName
        parcel.readDouble(),        // expenseAmt, use readDouble instead of readLong
        parcel.readString() ?: ""   // userName
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(expenseId)
        parcel.writeString(expenseName)
        parcel.writeDouble(expenseAmt)  // Corrected to write Double
        parcel.writeString(userName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExpenseModel> {
        override fun createFromParcel(parcel: Parcel): ExpenseModel {
            return ExpenseModel(parcel)
        }

        override fun newArray(size: Int): Array<ExpenseModel?> {
            return arrayOfNulls(size)
        }
    }
}
