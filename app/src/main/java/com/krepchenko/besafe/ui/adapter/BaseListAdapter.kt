package com.krepchenko.besafe.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import java.util.*

/**
 * Created by ann on 8/10/17.
 */

abstract class BaseListAdapter<T>(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    protected val list: MutableList<T> = ArrayList()
    protected var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: DataAdapterListener<T>? = null

    fun setDataListener(listener: DataAdapterListener<T>) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addItem(item: T) {
        this.list.add(item)
        notifyDataSetChanged()
        if (this.listener != null) {
            this.listener!!.onDataChanged(this.list)
        }
    }

    fun setList(list: List<T>) {
        this.list.addAll(list)
        notifyDataSetChanged()
        if (this.listener != null) {
            this.listener!!.onDataChanged(this.list)
        }
    }

    fun replaceList(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
        if (this.listener != null) {
            this.listener!!.onDataChanged(this.list)
        }
    }

    fun clearList() {
        list.clear()
        notifyDataSetChanged()
        if (this.listener != null) {
            this.listener!!.onDataChanged(this.list)
        }
    }

    fun updateItem(pos: Int, item: T) {
        if (pos < list.size) {
            list.removeAt(pos)
            list.add(pos, item)
            notifyItemChanged(pos, item)
        }
        if (this.listener != null) {
            this.listener!!.onDataChanged(this.list)
        }
    }

    fun removeItem(pos: Int) {
        if (pos < list.size) {
            list.removeAt(pos)
            notifyItemRemoved(pos)
        }
        if (this.listener != null) {
            this.listener!!.onDataChanged(this.list)
        }
    }

    fun removeItems(items: List<T>) {
        list.removeAll(items)
        notifyDataSetChanged()
        if (this.listener != null) {
            this.listener!!.onDataChanged(this.list)
        }
    }

    interface DataAdapterListener<T> {
        fun onDataChanged(data: List<T>)
    }

}
