package com.github.meaninglesscode.registerclient.models.api.product.entities

import com.github.meaninglesscode.registerclient.models.api.product.constants.ProductFieldNames
import com.squareup.moshi.Json

class ProductCreateEntity(lookupCode: String = "", name: String = "", description: String = "",
                          price: String = "", inventory: String = "")
{
    @Json(name = ProductFieldNames.LOOKUP_CODE)
    val lookupCode: String = lookupCode

    @Json(name = ProductFieldNames.NAME)
    val name: String = name

    @Json(name = ProductFieldNames.DESCRIPTION)
    val description: String = description

    @Json(name = ProductFieldNames.PRICE)
    val price: String = price

    @Json(name = ProductFieldNames.INVENTORY)
    val inventory: String = inventory
}