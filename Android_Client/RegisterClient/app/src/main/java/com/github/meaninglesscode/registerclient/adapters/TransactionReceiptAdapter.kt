package com.github.meaninglesscode.registerclient.adapters

import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.github.meaninglesscode.registerclient.activities.TransactionReceiptActivity
import com.github.meaninglesscode.registerclient.databinding.LayoutReceiptTransactionBinding
import com.github.meaninglesscode.registerclient.models.api.transaction.entities.TransactionEntryCreateEntity
import java.lang.ref.WeakReference

class TransactionReceiptAdapter(data: List<TransactionEntryCreateEntity>, reference: WeakReference<TransactionReceiptActivity>):
        RecyclerView.Adapter<TransactionReceiptAdapter.ViewHolder>()
{
    private val weakReference = reference.get()
    private val inflater = weakReference?.layoutInflater

    private var dataSet: SortedList<TransactionEntryCreateEntity> =
            SortedList(TransactionEntryCreateEntity::class.java, object: SortedList.Callback<TransactionEntryCreateEntity>()
    {
        override fun compare(o1: TransactionEntryCreateEntity, o2: TransactionEntryCreateEntity): Int {return o1.name.compareTo(o2.name) }

        override fun onInserted(position: Int, count: Int) { notifyItemRangeInserted(position, count) }

        override fun onRemoved(position: Int, count: Int) { notifyItemRangeRemoved(position, count) }

        override fun onMoved(fromPosition: Int, toPosition: Int) { notifyItemMoved(fromPosition, toPosition) }

        override fun onChanged(position: Int, count: Int) { notifyItemRangeChanged(position, count) }

        override fun areContentsTheSame(oldItem: TransactionEntryCreateEntity, newItem: TransactionEntryCreateEntity): Boolean { return oldItem == newItem }

        override fun areItemsTheSame(item1: TransactionEntryCreateEntity, item2: TransactionEntryCreateEntity): Boolean { return item1.lookupCode == item2.lookupCode }
    })

    init {
        dataSet.beginBatchedUpdates()
        for (i in dataSet.size() - 1 downTo 0) {
            val entity = dataSet.get(i)
            if (!data.contains(entity))
                dataSet.remove(entity)
        }
        dataSet.addAll(data)
        dataSet.endBatchedUpdates()
    }

    class ViewHolder(private val binding: LayoutReceiptTransactionBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(entryEntity: TransactionEntryCreateEntity) {
            binding.model = entryEntity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutReceiptTransactionBinding.inflate(inflater!!, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(dataSet.get(position))
    }

    override fun getItemCount(): Int {
        return dataSet.size()
    }

}