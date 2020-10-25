package com.github.meaninglesscode.registerclient.models.api.transaction.types

import android.os.Parcel
import android.os.Parcelable
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresSimpleDateFormat
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionEntity
import java.sql.Timestamp
import java.util.*

class Transaction(id: UUID = UUID.fromString(PostgresConstants.EMPTY_UUID.value),
                  cashierId: String = "", type: String = "", total: Double = -1.0,
                  referenceId: UUID = UUID.fromString(PostgresConstants.EMPTY_UUID.value),
                  createdOn: Timestamp = Timestamp(System.currentTimeMillis())): Parcelable
{
    var id: UUID = id
    var cashierId: String = cashierId
    var type: String = type
    var total: Double = total
    var referenceId: UUID = referenceId
    var createdOn: Timestamp = createdOn

    fun toTransactionEntity(): TransactionEntity {
        val createdOnString = "${createdOn.toString().replace(" ", "T")}Z"

        return TransactionEntity(id.toString(), type, cashierId, total.toString(),
                referenceId.toString(), createdOnString)
    }

    constructor(parcel: Parcel) : this() {
        id = UUID.fromString(parcel.readString())
        cashierId = parcel.readString()
        type = parcel.readString()
        total = parcel.readDouble()
        referenceId = UUID.fromString(parcel.readString())
        createdOn = Timestamp(PostgresSimpleDateFormat.parse(parcel.readString()).time)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id.toString())
        parcel.writeString(cashierId)
        parcel.writeString(type)
        parcel.writeDouble(total)
        parcel.writeString(referenceId.toString())
        parcel.writeString(createdOn.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Transaction> {
        override fun createFromParcel(parcel: Parcel): Transaction {
            return Transaction(parcel)
        }

        override fun newArray(size: Int): Array<Transaction?> {
            return arrayOfNulls(size)
        }
    }
}