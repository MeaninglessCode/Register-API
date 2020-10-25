package com.github.meaninglesscode.registerclient.models.api.product.types

import android.os.Parcel
import android.os.Parcelable
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresSimpleDateFormat
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import java.sql.Timestamp
import java.util.*

class Product(id: UUID = UUID.fromString(PostgresConstants.EMPTY_UUID.value), lookupCode: String = "",
              name: String = "", description: String = "", price: Double = -1.0, inventory: Int = -1,
              active: Boolean = true, createdOn: Timestamp = Timestamp(System.currentTimeMillis()),
              lastUpdate: Timestamp = Timestamp(System.currentTimeMillis())): Parcelable
{
    var id: UUID = id
    var lookupCode: String = lookupCode
    var name: String = name
    var description: String = description
    var price: Double = price
    var inventory: Int = inventory
    var active: Boolean = active
    var createdOn: Timestamp = createdOn
    var lastUpdate: Timestamp = lastUpdate

    fun toProductEntity(): ProductEntity {
        val createdString = "${createdOn.toString().replace(" ", "T")}Z"
        val updatedString = "${lastUpdate.toString().replace(" ", "T")}Z"

        return ProductEntity(id.toString(), lookupCode, name, description, price.toString(),
                inventory.toString(), active, createdString, updatedString)
    }

    constructor(parcel: Parcel) : this() {
        id = UUID.fromString(parcel.readString())
        lookupCode = parcel.readString()
        name = parcel.readString()
        description = parcel.readString()
        price = parcel.readDouble()
        inventory = parcel.readInt()
        active = parcel.readByte() != 0.toByte()
        createdOn = Timestamp(PostgresSimpleDateFormat.parse(parcel.readString()).time)
        lastUpdate = Timestamp(PostgresSimpleDateFormat.parse(parcel.readString()).time)

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id.toString())
        parcel.writeString(lookupCode)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(price)
        parcel.writeInt(inventory)
        parcel.writeByte(if (active) 1 else 0)
        parcel.writeString(createdOn.toString())
        parcel.writeString(lastUpdate.toString())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}