package com.github.meaninglesscode.registerclient.models.api.transaction.entities

import android.os.Parcel
import android.os.Parcelable
import com.github.meaninglesscode.registerclient.models.api.transaction.constants.TransactionCreateFieldNames
import com.squareup.moshi.Json

class TransactionEntryCreateEntity(lookupCode: String = "", name: String = "", price: String = "",
                                   discount: String = "", quantity: String = ""): Parcelable
{
    @Json(name = TransactionCreateFieldNames.LOOKUP_CODE)
    var lookupCode: String = lookupCode

    var name: String = name

    @Json(name = TransactionCreateFieldNames.PRICE)
    var price: String = price

    @Json(name = TransactionCreateFieldNames.DISCOUNT)
    var discount: String = discount

    @Json(name = TransactionCreateFieldNames.QUANTITY)
    var quantity: String = quantity

    override fun toString(): String {
        return "{" +
                "\"${TransactionCreateFieldNames.LOOKUP_CODE}\": \"${this.lookupCode}\"," +
                "\"${TransactionCreateFieldNames.PRICE}\": \"${this.price}\"," +
                "\"${TransactionCreateFieldNames.DISCOUNT}\": \"${this.discount}\"," +
                "\"${TransactionCreateFieldNames.QUANTITY}\": ${this.quantity}" +
                "}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        else if (other == null || this.javaClass != other.javaClass)
            return false

        val that = other as TransactionEntryCreateEntity?
        return (this.lookupCode == that!!.lookupCode) && (this.name == that.name) &&
                (this.price == that.price) && (this.discount == that.discount) &&
                (this.quantity == that.quantity)
    }

    // Parcelable implementation

    constructor(parcel: Parcel) : this() {
        lookupCode = parcel.readString()
        name = parcel.readString()
        price = parcel.readString()
        discount = parcel.readString()
        quantity = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(lookupCode)
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(discount)
        parcel.writeString(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionEntryCreateEntity> {
        override fun createFromParcel(parcel: Parcel): TransactionEntryCreateEntity {
            return TransactionEntryCreateEntity(parcel)
        }

        override fun newArray(size: Int): Array<TransactionEntryCreateEntity?> {
            return arrayOfNulls(size)
        }
    }
}