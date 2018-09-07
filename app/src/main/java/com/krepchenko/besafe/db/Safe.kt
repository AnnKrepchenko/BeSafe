package com.krepchenko.besafe.db

import android.os.Parcel
import android.os.Parcelable

data class Safe(var name: String = "", var pass: String = "", var login: String = "", var tel: String = "", var extraInfo: String = "", var email: String = "email@email.com", var serverId: String = "0") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(pass)
        parcel.writeString(login)
        parcel.writeString(tel)
        parcel.writeString(extraInfo)
        parcel.writeString(email)
        parcel.writeString(serverId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Safe> {
        override fun createFromParcel(parcel: Parcel): Safe {
            return Safe(parcel)
        }

        override fun newArray(size: Int): Array<Safe?> {
            return arrayOfNulls(size)
        }
    }
}