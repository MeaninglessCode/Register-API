package com.github.meaninglesscode.registerclient.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.adapters.TransactionProductListAdapter
import com.github.meaninglesscode.registerclient.models.api.constants.PostgresConstants
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionCreateEntity
import com.github.meaninglesscode.registerclient.models.constants.ParcelableNames
import com.github.meaninglesscode.registerclient.models.interfaces.BaseProductListActivity
import kotlinx.android.synthetic.main.activity_transaction_main.*
import kotlinx.android.synthetic.main.layout_product_transaction.view.*
import java.lang.ref.WeakReference
import java.text.DecimalFormat

class TransactionsMainActivity: BaseProductListActivity(), OnQueryTextListener {
    private lateinit var viewAdapter: TransactionProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_transaction_main)

        viewToHide = transactions_main_list
        progressBarToShow = transactions_main_progress

        initializeRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_transaction_main, menu)

        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setOnQueryTextListener(this)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
            R.id.action_checkout -> checkoutProducts()
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val newList = filter(productEntities, newText)
        viewAdapter.replaceAll(newList)
        return true
    }

    private fun checkoutProducts() {
        var cancel = false
        var focusView: View? = null

        val selectedItems = viewAdapter.getSelectedItems()

        if (selectedItems.count() <= 0) {
            return
        }
        else {
            for (i in 0..(recyclerView.childCount - 1)) {
                val view = recyclerView.getChildAt(i)

                view.product_quantity.error = null
                view.product_discount.error = null

                if (view.selected.isChecked) {
                    // TODO Add check to see if desired quantity > available quantity

                    if (view.product_quantity.text.isEmpty()) {
                        view.product_quantity.error =  getString(R.string.error_field_required)
                        focusView = view
                        cancel = true
                    }
                    else if (view.product_quantity.text.toString().toIntOrNull() == null) {
                        view.product_quantity.error = getString(R.string.error_invalid_quantity)
                        focusView = view
                        cancel = true
                    }

                    if (!view.product_discount.text.isEmpty()) {
                        if (view.product_discount.text.toString().toDoubleOrNull() == null) {
                            view.product_discount.error = getString(R.string.error_invalid_discount)
                            focusView = view
                            cancel = true
                        }
                    }
                }
            }

            if (cancel) {
                focusView?.requestFocus()
            }
            else {
                val employee = getCurrentEmployee()
                val items = viewAdapter.getSelectedItems()
                var total = 0.0

                for (item in items) {
                    if (item.discount.isBlank()) {
                        item.discount = "0"
                    }
                    total += ((item.price.toDouble() - (item.price.toDouble() * item.discount.toDouble())) * item.quantity.toDouble())
                }

                val df = DecimalFormat("#.##")
                total = java.lang.Double.valueOf(df.format(total))


                val transaction = TransactionCreateEntity(employee.employeeId, "sale",
                        total.toString(), PostgresConstants.EMPTY_UUID.value, items)

                val intent = Intent(this, TransactionReceiptActivity::class.java)
                        .putExtra(ParcelableNames.TRANSACTION_PRODUCTS, transaction)

                startActivity(intent)
            }
        }
    }

    private fun initializeRecyclerView() {
        val res = getAllProducts()

        viewManager = LinearLayoutManager(this)
        updateRecyclerView(res.second)
    }

    private fun updateRecyclerView(newData: List<ProductEntity>) {
        productEntities = newData
        viewAdapter = TransactionProductListAdapter(newData, WeakReference(this))

        recyclerView = findViewById<RecyclerView>(R.id.transactions_main_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }
}