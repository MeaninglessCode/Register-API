package com.github.meaninglesscode.registerclient.adapters

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import com.github.meaninglesscode.registerclient.activities.TransactionsMainActivity
import com.github.meaninglesscode.registerclient.databinding.LayoutProductTransactionBinding
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionEntryCreateEntity
import com.github.meaninglesscode.registerclient.models.interfaces.BaseProductListAdapter
import kotlinx.android.synthetic.main.layout_product_transaction.view.*
import java.lang.ref.WeakReference

class TransactionProductListAdapter(data: List<ProductEntity>, reference: WeakReference<TransactionsMainActivity>):
        BaseProductListAdapter<TransactionProductListAdapter.ViewHolder, TransactionsMainActivity>(data, reference)
{
    private val selectedItemData = mutableListOf<TransactionEntryCreateEntity>()

    class ViewHolder(private val binding: LayoutProductTransactionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ProductEntity) {
            binding.model = product
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionProductListAdapter.ViewHolder {
        val binding = LayoutProductTransactionBinding.inflate(inflater!!, parent, false)

        binding.root.setOnClickListener {
            binding.root.selected.isChecked = !binding.root.selected.isChecked

            if (binding.root.selected.isChecked) {
                binding.root.product_divider_lower.visibility = View.VISIBLE
                binding.root.product_transaction_info.visibility = View.VISIBLE
            }
            else {
                binding.root.product_divider_lower.visibility = View.GONE
                binding.root.product_transaction_info.visibility = View.GONE
            }

            for (i in dataSet.size() - 1 downTo 0) {
                if (dataSet.get(i).lookupCode == binding.root.product_lookup_code.text.toString()) {
                    var duplicateFound = false
                    val product = dataSet.get(i)

                    val transactionEntity = TransactionEntryCreateEntity(product.lookupCode, product.name, product.price,
                            binding.root.product_discount.text.toString(), binding.root.product_quantity.text.toString())

                    for (j in selectedItemData.size - 1 downTo 0) {
                        if (selectedItemData[j].lookupCode == transactionEntity.lookupCode) {
                            duplicateFound = true
                            selectedItemData.remove(selectedItemData[j])
                        }
                    }

                    if (!duplicateFound) {
                        selectedItemData.add(transactionEntity)
                    }

                    break
                }
            }
        }

        binding.root.product_discount.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                for (entity in selectedItemData) {
                    if (entity.lookupCode == binding.root.product_lookup_code.text.toString()) {
                        entity.discount = s.toString()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

        })

        binding.root.product_quantity.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                for (entity in selectedItemData) {
                    if (entity.lookupCode == binding.root.product_lookup_code.text.toString()) {
                        entity.quantity = s.toString()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

        })

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (this.itemCount == 1 && (dataSet[position].id == "")) {
            holder.bind(ProductEntity(name = "No products found!", inventory = "", price = ""))
        }
        else if (!dataSet[position].active) {
            return
        }
        else {
            holder.bind(dataSet.get(position))
        }
    }

    fun getSelectedItems(): List<TransactionEntryCreateEntity> {
        return selectedItemData.toList()
    }
}