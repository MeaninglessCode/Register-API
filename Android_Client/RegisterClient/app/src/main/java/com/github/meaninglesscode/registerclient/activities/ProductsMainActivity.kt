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
import com.github.meaninglesscode.registerclient.R
import com.github.meaninglesscode.registerclient.adapters.ProductListAdapter
import com.github.meaninglesscode.registerclient.models.api.constants.HTTPResponseCodes
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import com.github.meaninglesscode.registerclient.models.interfaces.BaseProductListActivity
import kotlinx.android.synthetic.main.activity_product_main.*
import java.lang.ref.WeakReference

class ProductsMainActivity: BaseProductListActivity(), OnQueryTextListener {
    private lateinit var viewAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_product_main)

        viewToHide = products_main_list
        progressBarToShow = products_main_progress

        initializeRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_product_main, menu)

        val searchView = menu.findItem(R.id.menu_search).actionView as SearchView

        searchView.setOnQueryTextListener(this)

        searchView.setOnCloseListener {
            menu.findItem(R.id.action_add).isVisible = true
            false
        }

        searchView.setOnSearchClickListener {
            menu.findItem(R.id.action_add).isVisible = false
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> NavUtils.navigateUpFromSameTask(this)
            R.id.action_refresh -> resetRecyclerView()
            R.id.action_add -> createNewProduct()
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        showProgress(true)

        return if (query == null || query.isBlank()) {
            showProgress(false)
            true
        }
        else {
            val result = doProductSearch(query)

            if (result.first == HTTPResponseCodes.OK) {
                updateRecyclerView(result.second)
            }

            showProgress(false)
            true
        }
    }

    override fun onQueryTextChange(newText: String): Boolean {
        val newList = filter(productEntities, newText)
        viewAdapter.replaceAll(newList)
        return true
    }

    private fun createNewProduct() {
        val intent = Intent(this@ProductsMainActivity, CreateProductActivity::class.java)
        startActivity(intent)
    }

    private fun initializeRecyclerView() {
        val res = getAllProducts()

        viewManager = LinearLayoutManager(this@ProductsMainActivity)
        updateRecyclerView(res.second)
    }

    private fun updateRecyclerView(newData: List<ProductEntity>) {
        productEntities = newData
        viewAdapter = ProductListAdapter(newData, WeakReference(this@ProductsMainActivity))

        recyclerView = findViewById<RecyclerView>(R.id.products_main_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    private fun resetRecyclerView() {
        initializeRecyclerView()
    }
}