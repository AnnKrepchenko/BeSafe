package com.krepchenko.besafe.ui.adapter

import com.krepchenko.besafe.db.Safe

interface ItemsClickListener {
    fun itemClicked(safe: Safe, position: Int)


}