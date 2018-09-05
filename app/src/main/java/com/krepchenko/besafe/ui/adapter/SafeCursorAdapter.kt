package com.krepchenko.besafe.ui.adapter

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.krepchenko.besafe.R
import com.krepchenko.besafe.db.Safe
import com.krepchenko.besafe.db.SafeEntity
import com.krepchenko.besafe.ui.activities.BaseActivity

/**
 * Created by Ann on 18.10.2015.
 */
class SafeCursorAdapter(context: Context,itemClickListener: ItemsClickListener) : BaseListAdapter<Safe>(context) {

    var itemClickListener: ItemsClickListener = itemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_safe, parent, false)
        return SafeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val safeViewHolder = holder as SafeViewHolder
        safeViewHolder.item_name!!.text = list[position].name

    }

    protected inner class SafeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var item_name: TextView? = null

        init {
            item_name = itemView.findViewById(R.id.item_name)
            itemView.findViewById<LinearLayout>(R.id.item_ll).setOnClickListener { itemClickListener.itemClicked(list[adapterPosition],adapterPosition) }
        }
    }

}
