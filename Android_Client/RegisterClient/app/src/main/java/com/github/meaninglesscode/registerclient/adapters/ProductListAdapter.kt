package com.github.meaninglesscode.registerclient.adapters

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.github.meaninglesscode.registerclient.activities.EditProductActivity
import com.github.meaninglesscode.registerclient.activities.ProductsMainActivity
import com.github.meaninglesscode.registerclient.databinding.LayoutProductBinding
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import com.github.meaninglesscode.registerclient.models.constants.ParcelableNames
import com.github.meaninglesscode.registerclient.models.interfaces.BaseProductListAdapter
import kotlinx.android.synthetic.main.layout_product.view.*
import java.lang.ref.WeakReference

class ProductListAdapter(data: List<ProductEntity>, reference: WeakReference<ProductsMainActivity>):
        BaseProductListAdapter<ProductListAdapter.ViewHolder, ProductsMainActivity>(data, reference)
{
    class ViewHolder(private val binding: LayoutProductBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.model = product
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListAdapter.ViewHolder {
        val binding = LayoutProductBinding.inflate(inflater!!, parent, false)

        binding.root.setOnClickListener {
            var productEntity = ProductEntity()

            for (i in dataSet.size() - 1 downTo 0) {
                if (dataSet.get(i).lookupCode == binding.root.product_lookup_code.text.toString()) {
                    productEntity = dataSet.get(i)
                    break
                }
            }

            val intent = Intent(weakReference, EditProductActivity::class.java)
                    .putExtra(ParcelableNames.PRODUCT_TO_VIEW, productEntity.toProduct())

            weakReference?.startActivity(intent)
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (this.itemCount == 1 && (dataSet[position].id == "")) {
            holder.bind(ProductEntity(name = "No products found!", inventory = "", price = ""))
        }
        else {
            holder.bind(dataSet.get(position))
        }
    }
}