package com.github.meaninglesscode.registerclient.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import com.github.meaninglesscode.registerclient.models.interfaces.BaseActivity
import com.github.meaninglesscode.registerclient.models.interfaces.RegisterApiClient
import com.github.meaninglesscode.registerclient.models.interfaces.handleError
import kotlinx.android.synthetic.main.activity_product_create.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking

class CreateProductActivity: BaseActivity() {
    private var createError: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_create)

        viewToHide = create_form
        progressBarToShow = create_progress

        save_product.setOnClickListener { attemptCreate() }
    }

    private fun attemptCreate() {
        lookup_code.error = null
        name.error = null
        description.error = null
        price.error = null
        inventory.error = null

        val lookupCodeStr = lookup_code.text.toString()
        val nameStr = name.text.toString()
        val descriptionStr = name.text.toString()
        val priceStr = price.text.toString()
        val inventoryStr = inventory.text.toString()

        createError = false
        var cancel = false
        var focusView: View? = null

        if (TextUtils.isEmpty(lookupCodeStr)) {
            lookup_code.error = getString(R.string.error_field_required)
            focusView = lookup_code
            cancel = true
        }

        if (TextUtils.isEmpty(nameStr)) {
            name.error = getString(R.string.error_field_required)
            focusView = name
            cancel = true
        }

        if (TextUtils.isEmpty(descriptionStr)) {
            description.error = getString(R.string.error_field_required)
            focusView = description
            cancel = true
        }

        if (TextUtils.isEmpty(priceStr)) {
            price.error = getString(R.string.error_field_required)
            focusView = price
            cancel = true
        }
        else if (priceStr.toDoubleOrNull() == null) {
            price.error = getString(R.string.error_invalid_price)
            focusView = price
            cancel = true
        }

        if (TextUtils.isEmpty(inventoryStr)) {
            inventory.error = getString(R.string.error_field_required)
            focusView = inventory
            cancel = true
        }
        else if (inventoryStr.toIntOrNull() == null) {
            inventory.error = getString(R.string.error_invalid_inventory)
            focusView = inventory
            cancel = true
        }

        if (cancel) {
            focusView?.requestFocus()
        }
        else
        {
            showProgress(true)

            runBlocking(CommonPool) {
                createProductTask(lookupCodeStr, nameStr, descriptionStr, priceStr, inventoryStr)
            }

            if (createError) {
                showProgress(false)
            }
            else {
                finish()
            }
        }
        showProgress(false)
    }

    private suspend fun createProductTask(lookupCode: String, name: String, description: String,
                                          price: String, inventory: String)
    {
        lateinit var result: Pair<Int, ProductEntity?>

        runBlocking(CommonPool) {
            result = RegisterApiClient.createProduct(lookupCode, name, description, price, inventory)
        }

        when {
            result.first == HTTPResponseCodes.OK -> {
                return
            }
            result.first == HTTPResponseCodes.CONFLICT -> {
                lookup_code.error = getString(R.string.error_lookupCodeInUse)
                lookup_code.requestFocus()
                createError = true
            }
            result.first == HTTPResponseCodes.UNPROCESSABLE_ENTITY -> {
                lookup_code.error = getString(R.string.error_lookupCodeInvalid)
                lookup_code.requestFocus()
                createError = true
            }
            else -> {
                handleError(this@CreateProductActivity, result.first)
                createError = true
            }
        }
    }
}