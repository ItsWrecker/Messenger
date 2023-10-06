package com.qxlabai.presentation.xmpp.services

import android.os.Parcel
import android.os.Parcelable

data class CredentialsParcel(
    val uuid: String,
    val passcode: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )


    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(uuid)
        dest.writeString(passcode)
    }

    companion object CREATOR : Parcelable.Creator<CredentialsParcel> {
        override fun createFromParcel(parcel: Parcel): CredentialsParcel {
            return CredentialsParcel(parcel)
        }

        override fun newArray(size: Int): Array<CredentialsParcel?> {
            return arrayOfNulls(size)
        }
    }


}