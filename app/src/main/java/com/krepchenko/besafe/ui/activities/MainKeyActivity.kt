package com.krepchenko.besafe.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.firestore.FirebaseFirestore
import com.krepchenko.besafe.R
import com.krepchenko.besafe.crypt.CryptManager
import com.krepchenko.besafe.db.Safe
import com.krepchenko.besafe.db.SafeEntity
import com.krepchenko.besafe.ui.adapter.ItemsClickListener
import com.krepchenko.besafe.ui.adapter.SafeCursorAdapter
import kotlinx.android.synthetic.main.content_main.*

open class MainKeyActivity : BaseKeyActivity(), View.OnClickListener, ItemsClickListener {

    private var adapter: SafeCursorAdapter? = null
    private lateinit var emptyView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(BaseKeyActivity.KEY_PASS))
            encryptedPass = intent.getStringExtra(BaseKeyActivity.KEY_PASS)
        initViews()
    }

    internal override fun setLayout() {
        setContentView(R.layout.activity_main)
    }

    private fun initViews() {
        findViewById<View>(R.id.fab).setOnClickListener { AddKeyActivity.launch(this@MainKeyActivity, encryptedPass) }
        emptyView = layoutInflater.inflate(R.layout.view_empty, null)
        emptyView.findViewById<View>(R.id.empty_link).setOnClickListener(this)
        addContentView(emptyView, main_recyclerview!!.layoutParams)
        main_recyclerview.layoutManager = LinearLayoutManager(this)
        initAdapter()
    }

    override fun onResume() {
        super.onResume()
        clearAdapter()
        FirebaseFirestore.getInstance().collection(SafeEntity.TABLE_NAME)
                .whereEqualTo(SafeEntity.SECRET_FIELD, CryptManager.getSecretField(encryptedPass))
                .whereEqualTo(SafeEntity.EMAIL, GoogleSignIn.getLastSignedInAccount(this)!!.email)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val safes = mutableListOf<Safe>()
                        for (document in task.result) {
                            Log.d(TAG, document.id + " => " + document.data)
                            safes.add(Safe(document.data[SafeEntity.NAME] as String, document.data[SafeEntity.PASS] as String, document.data[SafeEntity.LOGIN] as String, document.data[SafeEntity.TEL] as String, document.data[SafeEntity.EXTRA_INFORMATION] as String, GoogleSignIn.getLastSignedInAccount(this)!!.email!!))
                        }
                        updateAdapter(safes)
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }
    }

    private fun initAdapter() {
        adapter = SafeCursorAdapter(this, this)
        main_recyclerview!!.adapter = adapter
    }

    private fun updateAdapter(list: MutableList<Safe>?) {
        list?.let {
            if (list.size > 0) {
                emptyView.visibility = View.GONE
                adapter?.setList(list)
            } else {
                emptyView.visibility = View.VISIBLE
            }
        }
    }

    private fun clearAdapter(){
        adapter?.clearList()
        emptyView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.empty_link -> {
                PinActivity.launch(this)
                finish()
            }
        }
    }

    override fun itemClicked(safe: Safe, position: Int) {
        Log.e("fuck", "clicked")
        ViewKeyActivity.launch(this, safe, encryptedPass)
    }

    companion object {



        fun launch(activity: Activity, pass: String) {
            val intent = Intent(activity, MainKeyActivity::class.java)
            intent.putExtra(BaseKeyActivity.KEY_PASS, pass)
            activity.startActivity(intent)
        }
    }
    /*

*/


}
