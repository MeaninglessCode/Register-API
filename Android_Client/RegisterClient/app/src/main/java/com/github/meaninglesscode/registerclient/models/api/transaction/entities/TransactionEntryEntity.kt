package com.github.meaninglesscode.registerclient.models.api.transaction.entities

import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.transaction.constants.TransactionEntryFieldNames
import com.github.meaninglesscode.registerclient.models.api.transaction.types.TransactionEntry
import com.squareup.moshi.Json
import java.util.*

class TransactionEntryEntity(id: String = "", transactionId: String = "", lookupCode: String = "",
                             price: String = "-1", discount: String = "0", quantity: String = "-1")
{
    @Json(name = TransactionEntryFieldNames.ID)
    var id: String = id

    @Json(name = TransactionEntryFieldNames.TRANSACTION_ID)
    var transactionId: String = transactionId

    @Json(name = TransactionEntryFieldNames.LOOKUP_CODE)
    var lookupCode: String = lookupCode

    @Json(name = TransactionEntryFieldNames.PRICE)
    var price: String = price

    @Json(name = TransactionEntryFieldNames.DISCOUNT)
    var discount: String = discount

    @Json(name = TransactionEntryFieldNames.QUANTITY)
    var quantity: String = quantity

    override fun toString(): String {
        return "{" +
                "\"${TransactionEntryFieldNames.ID}\": \"${this.id}\"," +
                "\"${TransactionEntryFieldNames.TRANSACTION_ID}\": \"${this.transactionId}\"," +
                "\"${TransactionEntryFieldNames.LOOKUP_CODE}\": \"${this.lookupCode}\"," +
                "\"${TransactionEntryFieldNames.PRICE}\": ${this.price}," +
                "\"${TransactionEntryFieldNames.DISCOUNT}\": \"${this.discount}\"" +
                "\"${TransactionEntryFieldNames.QUANTITY}\": ${this.quantity}" +
                "}"
    }

    fun toTransactionEntry(): TransactionEntry {
        val newId = if (id == "") UUID.fromString(PostgresConstants.EMPTY_UUID.value)
            else UUID.fromString(id)

        val newTransactionId = if (transactionId == "") UUID.fromString(PostgresConstants.EMPTY_UUID.value)
            else UUID.fromString(transactionId)

        val newPrice = price.toDouble()
        val newQuantity = quantity.toInt()

        return TransactionEntry(newId, newTransactionId, lookupCode, newPrice, newQuantity)
    }
}