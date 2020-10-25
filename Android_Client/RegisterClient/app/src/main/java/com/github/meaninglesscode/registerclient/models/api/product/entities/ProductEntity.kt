package com.github.meaninglesscode.registerclient.models.api.product.entities

import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresSimpleDateFormat
import com.github.meaninglesscode.registerclient.models.api.product.constants.ProductFieldNames
import com.github.meaninglesscode.registerclient.models.api.product.types.Product
import com.squareup.moshi.Json
import java.sql.Timestamp
import java.util.*

class ProductEntity(id: String = "", lookupCode: String = "", name: String = "", description: String = "",
                    price: String = "-1", inventory: String = "-1", active: Boolean = true,
                    createdOn: String = "", lastUpdate: String = "")
{
    @Json(name = ProductFieldNames.ID)
    var id: String = id

    @Json(name = ProductFieldNames.LOOKUP_CODE)
    var lookupCode: String = lookupCode

    @Json(name = ProductFieldNames.NAME)
    var name: String = name

    @Json(name = ProductFieldNames.DESCRIPTION)
    var description : String = description

    @Json(name = ProductFieldNames.PRICE)
    var price: String = price

    @Json(name = ProductFieldNames.INVENTORY)
    var inventory: String = inventory

    @Json(name = ProductFieldNames.ACTIVE)
    var active: Boolean = active

    @Json(name = ProductFieldNames.CREATED_ON)
    var createdOn: String = createdOn

    @Json(name = ProductFieldNames.LAST_UPDATE)
    var lastUpdate: String = lastUpdate

    override fun toString(): String {
        return "{" +
                "\"${ProductFieldNames.ID}\": \"${this.id}\"," +
                "\"${ProductFieldNames.LOOKUP_CODE}\": \"${this.lookupCode}\"," +
                "\"${ProductFieldNames.NAME}\": \"${this.name}\"," +
                "\"${ProductFieldNames.DESCRIPTION}\": \"${this.description}\"," +
                "\"${ProductFieldNames.PRICE}\": ${this.price}," +
                "\"${ProductFieldNames.INVENTORY}\": ${this.inventory}," +
                "\"${ProductFieldNames.ACTIVE}\": ${this.active}," +
                "\"${ProductFieldNames.CREATED_ON}\": \"${this.createdOn}\"," +
                "\"${ProductFieldNames.LAST_UPDATE}\": \"${this.lastUpdate}\"" +
                "}"
    }

    fun toProduct(): Product {
        val newId = if (id == "") UUID.fromString(PostgresConstants.EMPTY_UUID.value)
            else UUID.fromString(id)

        val newPrice = price.toDouble()

        val newCreated = createdOn.replace("T", " ").replace("Z", "")
        val newUpdated = lastUpdate.replace("T", " ").replace("Z", "")
        val createdTimestamp = Timestamp(PostgresSimpleDateFormat.parse(newCreated).time)
        val updatedTimestamp = Timestamp(PostgresSimpleDateFormat.parse(newUpdated).time)

        return Product(newId, lookupCode, name, description, newPrice, inventory.toInt(),
                active, createdTimestamp, updatedTimestamp)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        else if (other == null || this.javaClass != other.javaClass)
            return false

        val that = other as ProductEntity?
        return (this.id == that!!.id) && (this.lookupCode == that.lookupCode) &&
                (this.name == that.name) && (this.description == that.description) &&
                (this.price == that.price) && (this.inventory == that.inventory) &&
                (this.active == that.active) && (this.createdOn == that.createdOn) &&
                (this.lastUpdate == that.lastUpdate)
    }

    fun alphabeticalComparator(other: ProductEntity): Int {
        return this.name.compareTo(other.name)
    }
}