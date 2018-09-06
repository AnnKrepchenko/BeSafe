package com.krepchenko.besafe.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.firestore.FirebaseFirestore
import com.krepchenko.besafe.R
import com.krepchenko.besafe.crypt.CryptManager
import com.krepchenko.besafe.db.Safe
import com.krepchenko.besafe.db.SafeEntity
import kotlinx.android.synthetic.main.content_view.*

/**
 * Created by Ann on 18.10.2015.
 */
class ViewKeyActivity : BaseKeyActivity() {
    private lateinit var safe: Safe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(BaseKeyActivity.KEY_PASS))
            encryptedPass = intent.getStringExtra(BaseKeyActivity.KEY_PASS)
        if (intent.hasExtra(KEY_SAFE))
            safe = intent.getParcelableExtra(KEY_SAFE)
        initBack()
    }

    override fun onResume() {
        super.onResume()
        setContent()
    }

    internal override fun setLayout() {
        setContentView(R.layout.activity_view)
    }

    private fun setContent() {
        view_name_tv.text = safe.name
        view_login_tv.text = CryptManager.decrypt(encryptedPass, safe.login)
        view_pass_tv.text = CryptManager.decrypt(encryptedPass, safe.pass)
        view_tel_tv.text = CryptManager.decrypt(encryptedPass, safe.tel)
        view_extra_tv.text = CryptManager.decrypt(encryptedPass, safe.extraInfo)
        view_extra_tv.movementMethod = ScrollingMovementMethod()
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_edit -> {
                 EditKeyActivity.launch(this, safe, encryptedPass)
                finish()
                return true
            }
            R.id.action_delete -> {
                deleteAction()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAction() {
        val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
        builder.setMessage(getString(R.string.alert_delete) + " " + safe.name + "?")
        builder.setPositiveButton(R.string.alert_btn_ok) { _, _ ->
            startLoadingDialog()
            FirebaseFirestore.getInstance().collection(SafeEntity.TABLE_NAME)
                    .document(safe.serverId).delete()
                    .addOnCompleteListener { task ->
                        stopLoadingDialog()
                        if (task.isSuccessful) {
                            MainKeyActivity.launch(this,encryptedPass)
                            finish()
                        } else {
                            Log.w(TAG, "Error getting documents.", task.exception)
                        }
                    }
        }
        builder.setNegativeButton(R.string.alert_btn_cancel, null)
        builder.show()
    }

    companion object {

        private val KEY_SAFE = "safe"

        fun launch(activity: Activity, safe: Safe, pass: String) {
            val intent = Intent(activity, ViewKeyActivity::class.java)
            intent.putExtra(KEY_SAFE, safe)
            intent.putExtra(BaseKeyActivity.KEY_PASS, pass)
            activity.startActivity(intent)
        }
    }
}
