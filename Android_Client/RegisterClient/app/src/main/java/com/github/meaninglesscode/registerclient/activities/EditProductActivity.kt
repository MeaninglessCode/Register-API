package com.github.meaninglesscode.registerclient.activities

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextUtils
import android.view.View
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresHumanReadableDateFormat
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import com.github.meaninglesscode.registerclient.models.api.product.types.Product
import com.github.meaninglesscode.registerclient.models.constants.ParcelableNames
import com.github.meaninglesscode.registerclient.models.interfaces.RegisterApiClient
import com.github.meaninglesscode.registerclient.models.interfaces.handleError
import kotlinx.android.synthetic.main.activity_product_view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import java.sql.Timestamp

class EditProductActivity: AppCompatActivity() {
    private var errorOccurred: Boolean = false
    private lateinit var currentProduct: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_view)

        currentProduct = intent.getParcelableExtra(ParcelableNames.PRODUCT_TO_VIEW)

        setInitialValues()

        save_product_button.setOnClickListener { attemptUpdate() }
        delete_product_button.setOnClickListener { deleteConfirmDialog() }
    }

    private fun deleteConfirmDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirm").setMessage("Are you sure you want to delete this product?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .create()
                .show()
    }

    private val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when(which) {
            DialogInterface.BUTTON_POSITIVE -> attemptDelete()
            DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
        }
    }

    private fun setInitialValues() {
        lookup_code.text = Editable.Factory().newEditable(currentProduct.lookupCode)
        name.text = Editable.Factory().newEditable(currentProduct.name)
        description.text = Editable.Factory().newEditable(currentProduct.description)
        price.text = Editable.Factory().newEditable(currentProduct.price.toString())
        inventory.text = Editable.Factory().newEditable(currentProduct.inventory.toString())
        active_switch.isChecked = currentProduct.active

        created_on.text = Editable.Factory().newEditable(
                PostgresHumanReadableDateFormat.format(currentProduct.createdOn))
        created_on.isEnabled = false

        last_update.text = Editable.Factory().newEditable(
                PostgresHumanReadableDateFormat.format(currentProduct.lastUpdate))
        last_update.isEnabled = false
    }

    private fun attemptUpdate() {
        lookup_code.error = null
        name.error = null
        description.error = null
        price.error = null
        inventory.error = null

        val lookupCodeStr = lookup_code.text.toString()
        val nameStr = name.text.toString()
        val descriptionStr = description.text.toString()
        val priceStr = price.text.toString()
        val inventoryStr = inventory.text.toString()

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
            price.error = getString(R.string.error_invalid_price)
            focusView = price
            cancel = true
        }
        else if (priceStr.toDoubleOrNull() == null) {
            price.error = getString(R.string.error_field_required)
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
        else {
            val result = async(CommonPool) {
                doProductUpdate()
            }

            if (result.isCancelled || errorOccurred) {
                return
            }
            else {
                finish()
            }
        }
    }

    private fun attemptDelete() {
        val result = async(CommonPool) { doProductDeletion() }

        if (result.isCancelled || errorOccurred) {
            return
        }
        else {
            finish()
        }
    }

    private suspend fun doProductDeletion() {
        var result = 0

        runBlocking(CommonPool) {
            result = RegisterApiClient.deleteProduct(currentProduct.id.toString())
        }

        if (result != HTTPResponseCodes.OK) {
            errorOccurred = true
        }
    }

    private suspend fun doProductUpdate(): Int {
        val newEntity = currentProduct.toProductEntity()
        val updateTime = Timestamp(System.currentTimeMillis())

        newEntity.lookupCode = lookup_code.text.toString()
        newEntity.name = name.text.toString()
        newEntity.description = description.text.toString()
        newEntity.price = price.text.toString()
        newEntity.inventory = inventory.text.toString()
        newEntity.active = active_switch.isChecked
        newEntity.lastUpdate = "${updateTime.toString().replace(" ", "T")}Z"

        lateinit var result: Pair<Int, ProductEntity?>

        runBlocking(CommonPool) {
            result = RegisterApiClient.updateProduct(newEntity.id, newEntity)
        }

        if (result.first != HTTPResponseCodes.OK) {
            errorOccurred = true
            handleError(this@EditProductActivity, result.first)
        }

        return result.first
    }
}