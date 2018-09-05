package com.krepchenko.besafe.ui.activities

import android.app.Activity
import android.app.LoaderManager
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.krepchenko.besafe.R
import com.krepchenko.besafe.core.SafeApplication
import com.krepchenko.besafe.crypt.CryptManager
import com.krepchenko.besafe.db.SafeEntity

/**
 * Created by Ann on 19.10.2015.
 */
class PinActivity : AppCompatActivity(), TextView.OnEditorActionListener, TextWatcher, LoaderManager.LoaderCallbacks<Cursor> {




    private var etPass: EditText? = null
    private var application: SafeApplication? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = applicationContext as SafeApplication
        setContentView(R.layout.activity_pin)
        initViews()
    }

    private fun initViews() {
        etPass = findViewById(R.id.pin_pass_et)
        etPass!!.setOnEditorActionListener(this)
        etPass!!.addTextChangedListener(this)
        if (!application!!.sharedManager.isHintViewed) {
            findViewById<View>(R.id.pin_user_hint).visibility = View.VISIBLE
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_DONE || v.text.length == 4) {
            if (v.id == R.id.pin_pass_et) {
                if (etPass!!.text.length != 4) {
                    etPass!!.error = getString(R.string.error_pin)
                } else {
                    submitText()
                }
            }
        }
        return false
    }

    private fun submitText() {
        (applicationContext as SafeApplication).sharedManager.setHintViewed()

        dbSetup()

        MainKeyActivity.launch(this, CryptManager.getEncryptedPass(etPass!!.text.toString()))
    }

    private fun dbSetup() {


    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.length == 4) {
            (applicationContext as SafeApplication).sharedManager.setHintViewed()
            MainKeyActivity.launch(this, CryptManager.getEncryptedPass(etPass!!.text.toString()))
        }
    }

    override fun afterTextChanged(s: Editable) {

    }

    companion object {

        fun launch(activity: Activity) {
            val intent = Intent(activity, PinActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args)
    {

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data )
    {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader)
    {
        adapter.changeCursor(null);
    }
}
