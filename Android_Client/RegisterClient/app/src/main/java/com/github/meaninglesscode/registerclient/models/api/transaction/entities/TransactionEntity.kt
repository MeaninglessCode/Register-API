package com.github.meaninglesscode.registerclient.models.api.transaction.entities

import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresSimpleDateFormat
import com.github.meaninglesscode.registerclient.models.api.transaction.constants.TransactionFieldNames
import com.github.meaninglesscode.registerclient.models.api.transaction.types.Transaction
import com.squareup.moshi.Json
import java.sql.Timestamp
import java.util.*

class TransactionEntity(id: String = "", cashierId: String = "", type: String = "",
                        total: String = "-1", referenceId: String = "", createdOn: String = "")
{
    @Json(name = TransactionFieldNames.ID)
    var id: String = id

    @Json(name = TransactionFieldNames.CASHIER_ID)
    var cashierId: String = cashierId

    @Json(name = TransactionFieldNames.TYPE)
    var type: String = type

    @Json(name = TransactionFieldNames.TOTAL)
    var total: String = total

    @Json(name = TransactionFieldNames.REFERENCE_ID)
    var referenceId: String = referenceId

    @Json(name = TransactionFieldNames.CREATED_ON)
    var createdOn: String = createdOn

    override fun toString(): String {
        return "{" +
                "\"${TransactionFieldNames.ID}\": \"${this.id}\"," +
                "\"${TransactionFieldNames.CASHIER_ID}\": \"${this.cashierId}\"," +
                "\"${TransactionFieldNames.TYPE}\": \"${this.type}\"," +
                "\"${TransactionFieldNames.TOTAL}\": ${this.total}," +
                "\"${TransactionFieldNames.REFERENCE_ID}\": \"${this.referenceId}\"," +
                "\"${TransactionFieldNames.CREATED_ON}\": \"${this.createdOn}\"" +
                "}"
    }

    fun toTransaction(): Transaction {
        val newId = if (id == "") UUID.fromString(PostgresConstants.EMPTY_UUID.value)
            else UUID.fromString(id)

        val newReferenceId = if (referenceId == "") UUID.fromString(PostgresConstants.EMPTY_UUID.value)
            else UUID.fromString(referenceId)

        val newTotal = total.toDouble()

        val newCreated = createdOn.replace("T", " ").replace("Z", "")
        val createdTimestamp = Timestamp(PostgresSimpleDateFormat.parse(newCreated).time)

        return Transaction(newId, cashierId, type, newTotal, newReferenceId, createdTimestamp)
    }
}