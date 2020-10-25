package com.github.meaninglesscode.registerclient.models.interfaces

import android.support.v7.widget.RecyclerView
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.runBlocking

abstract class BaseProductListActivity: BaseActivity() {
    protected lateinit var recyclerView: RecyclerView
    protected lateinit var viewManager: RecyclerView.LayoutManager
    protected lateinit var productEntities: List<ProductEntity>

    protected fun filter(data: List<ProductEntity>, input: String): List<ProductEntity> {
        return data.filter {
            it.lookupCode.contains(input, true) || it.name.contains(input, true) ||
                    it.description.contains(input, true)
        }
    }

    protected fun getAllProducts(): Pair<Int, List<ProductEntity>> {
        lateinit var res: Pair<Int, List<ProductEntity>?>

        runBlocking(CommonPool) {
            res = RegisterApiClient.getAllProducts()
        }

        return if (res.second != null) Pair(res.first, res.second!!)
            else Pair(res.first, listOf(ProductEntity()))
    }

    protected fun doProductSearch(term: String): Pair<Int, List<ProductEntity>> {
        lateinit var res: Pair<Int, List<ProductEntity>?>

        runBlocking(CommonPool) {
            res = RegisterApiClient.searchProductsByTerm(term)
        }

        return if (res.second != null) Pair(res.first, res.second!!)
            else Pair(res.first, listOf(ProductEntity()))
    }
}