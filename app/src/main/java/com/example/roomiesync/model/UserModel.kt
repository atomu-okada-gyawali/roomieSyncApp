package com.example.roomiesync.model

import android.os.Parcel
import android.os.Parcelable

class UserModel (
    var userId : String = "",
    var name : String = "",
    var contact : String = "",
    var email : String = "",
    var photo : Int = 0,
    var address : String = "",
    var password : String = ""
    ): Parcelable {
    constructor(parcel: Parcel):this(

    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(name)
        parcel.writeString(contact)
        parcel.writeString(email)
        parcel.writeInt(photo)
        parcel.writeString(address)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }
}



