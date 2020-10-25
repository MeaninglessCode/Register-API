package com.github.meaninglesscode.registerclient.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.widget.Toast
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.adapters.TransactionReceiptAdapter
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionCreateEntity
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionEntryCreateEntity
import com.github.meaninglesscode.registerclient.models.constants.ParcelableNames
import com.github.meaninglesscode.registerclient.models.interfaces.BaseActivity
import com.github.meaninglesscode.registerclient.models.interfaces.RegisterApiClient
import com.github.meaninglesscode.registerclient.models.interfaces.handleError
import kotlinx.android.synthetic.main.activity_transaction_receipt.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking
import java.lang.ref.WeakReference


class TransactionReceiptActivity: BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var transactionData: TransactionCreateEntity
    private lateinit var viewAdapter: TransactionReceiptAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_receipt)

        transactionData = intent.getParcelableExtra(ParcelableNames.TRANSACTION_PRODUCTS)

        initializeRecyclerView()
        setInitialValues()

        save_transaction_button.setOnClickListener { saveConfirmDialog() }
        cancel_transaction_button.setOnClickListener { finish() }
    }

    private fun saveConfirmDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirm").setMessage("Are you sure this transaction is correct?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener)
                .create()
                .show()
    }

    private val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when(which) {
            DialogInterface.BUTTON_POSITIVE -> saveTransaction()
            DialogInterface.BUTTON_NEGATIVE -> dialog.dismiss()
        }
    }

    private fun initializeRecyclerView() {
        viewManager = LinearLayoutManager(this)
        updateRecyclerView(transactionData.entries)
    }

    private fun setInitialValues() {
        cashier_id.text = transactionData.cashierId
        transaction_type.text = transactionData.type
        total_price.text = getString(R.string.text_view_total, transactionData.total)
    }

    private fun updateRecyclerView(newData: List<TransactionEntryCreateEntity>) {
        viewAdapter = TransactionReceiptAdapter(newData, WeakReference(this))

        recyclerView = findViewById<RecyclerView>(R.id.transaction_entry_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun saveTransaction() {
        var result = 0

        runBlocking(CommonPool) {
            result = RegisterApiClient.createTransaction(transactionData)
        }

        when (result) {
            HTTPResponseCodes.OK -> {
                val toast = Toast.makeText(this, "Transaction saved successfully!", Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()

                val intent = Intent(this, MainMenuActivity::class.java)
                startActivity(intent)
            }
            else -> {
                handleError(this, result)
            }
        }
    }
}