package com.github.meaninglesscode.registerclient.models.api.transaction.types

import android.os.Parcel
import android.os.Parcelable
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionEntryEntity
import java.util.*

class TransactionEntry(id: UUID = UUID.fromString(PostgresConstants.EMPTY_UUID.value),
                       transactionId: UUID = UUID.fromString(PostgresConstants.EMPTY_UUID.value),
                       lookupCode: String = "", price: Double = -1.0, quantity: Int = -1): Parcelable
{
    var id: UUID = id
    var transactionId: UUID = transactionId
    var lookupCode: String = lookupCode
    var price: Double = price
    var quantity: Int = quantity

    fun toTransactionEntryEntity(): TransactionEntryEntity {
        return TransactionEntryEntity(id.toString(), transactionId.toString(), lookupCode,
                price.toString(), quantity.toString())
    }

    constructor(parcel: Parcel) : this() {
        id = UUID.fromString(parcel.readString())
        transactionId = UUID.fromString(parcel.readString())
        lookupCode = parcel.readString()
        price = parcel.readDouble()
        quantity = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id.toString())
        parcel.writeString(transactionId.toString())
        parcel.writeString(lookupCode)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionEntry> {
        override fun createFromParcel(parcel: Parcel): TransactionEntry {
            return TransactionEntry(parcel)
        }

        override fun newArray(size: Int): Array<TransactionEntry?> {
            return arrayOfNulls(size)
        }
    }
}