package com.github.meaninglesscode.registerclient.models.interfaces

import android.support.v7.app.AppCompatActivity
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import com.github.meaninglesscode.registerclient.models.api.product.entities.ProductEntity
import java.lang.ref.WeakReference

abstract class BaseProductListAdapter<T: RecyclerView.ViewHolder, U: AppCompatActivity>
    (data: List<ProductEntity>, reference: WeakReference<U>): RecyclerView.Adapter<T>()
{
    protected val weakReference = reference.get()
    protected val inflater = weakReference?.layoutInflater

    protected var dataSet: SortedList<ProductEntity> = SortedList(ProductEntity::class.java, object: SortedList.Callback<ProductEntity>() {
        override fun compare(o1: ProductEntity, o2: ProductEntity): Int { return o1.alphabeticalComparator(o2) }

        override fun onInserted(position: Int, count: Int) { notifyItemRangeInserted(position, count) }

        override fun onRemoved(position: Int, count: Int) { notifyItemRangeRemoved(position, count) }

        override fun onMoved(fromPosition: Int, toPosition: Int) { notifyItemMoved(fromPosition, toPosition) }

        override fun onChanged(position: Int, count: Int) { notifyItemRangeChanged(position, count) }

        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean { return oldItem == newItem }

        override fun areItemsTheSame(item1: ProductEntity, item2: ProductEntity): Boolean { return item1.id == item2.id }
    })

    init { this.replaceAll(data) }

    fun add(entity: ProductEntity) {
        dataSet.add(entity)
    }

    fun remove(entity: ProductEntity) {
        dataSet.remove(entity)
    }

    fun add(entities: List<ProductEntity>) {
        dataSet.addAll(entities)
    }

    fun remove(entities: List<ProductEntity>) {
        dataSet.beginBatchedUpdates()
        for (entity in entities)
            dataSet.remove(entity)
        dataSet.endBatchedUpdates()
    }

    fun replaceAll(newData: List<ProductEntity>) {
        dataSet.beginBatchedUpdates()
        for (i in dataSet.size() - 1 downTo 0) {
            val entity = dataSet.get(i)
            if (!newData.contains(entity))
                dataSet.remove(entity)
        }
        dataSet.addAll(newData)
        dataSet.endBatchedUpdates()
    }

    override fun getItemCount() = dataSet.size()
}