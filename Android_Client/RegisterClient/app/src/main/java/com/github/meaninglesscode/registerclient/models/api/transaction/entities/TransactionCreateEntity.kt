package com.github.meaninglesscode.registerclient.models.api.transaction.entities

import android.os.Parcel
import android.os.Parcelable
import com.github.meaninglesscode.registerclient.models.api.transaction.constants.TransactionCreateFieldNames
import com.squareup.moshi.Json

class TransactionCreateEntity(cashierId: String = "", type: String = "", total: String = "",referenceId: String = "",
                              entries: List<TransactionEntryCreateEntity> = listOf()): Parcelable
{
    @Json(name = TransactionCreateFieldNames.CASHIER_ID)
    var cashierId: String = cashierId

    @Json(name = TransactionCreateFieldNames.TYPE)
    var type: String = type

    @Json(name = TransactionCreateFieldNames.TOTAL)
    var total: String = total

    @Json(name = TransactionCreateFieldNames.REFERENCE_ID)
    var referenceId: String = referenceId

    @Json(name = TransactionCreateFieldNames.ENTRIES)
    var entries: List<TransactionEntryCreateEntity> = entries

    override fun toString(): String {
        var string = "{" +
                "\"${TransactionCreateFieldNames.CASHIER_ID}\": \"${this.cashierId}\"," +
                "\"${TransactionCreateFieldNames.TYPE}\": \"${this.type}\"," +
                "\"${TransactionCreateFieldNames.TOTAL}\": \"${this.total}\"," +
                "\"${TransactionCreateFieldNames.REFERENCE_ID}\": \"${this.referenceId}\"," +
                "\"${TransactionCreateFieldNames.ENTRIES}\": ["

        for (entry in entries) {
            string += if (entries.indexOf(entry) == (entries.size - 1)) entry.toString()
            else "$entry, "
        }

        string += "]}"
        return string
    }

    // Parcelable implementation

    constructor(parcel: Parcel) : this() {
        cashierId = parcel.readString()
        type = parcel.readString()
        total = parcel.readString()
        referenceId = parcel.readString()
        entries = parcel.createTypedArrayList(TransactionEntryCreateEntity)
    }



    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(cashierId)
        parcel.writeString(type)
        parcel.writeString(total)
        parcel.writeString(referenceId)
        parcel.writeTypedList(entries)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionCreateEntity> {
        override fun createFromParcel(parcel: Parcel): TransactionCreateEntity {
            return TransactionCreateEntity(parcel)
        }

        override fun newArray(size: Int): Array<TransactionCreateEntity?> {
            return arrayOfNulls(size)
        }
    }
}