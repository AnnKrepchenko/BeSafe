package com.krepchenko.besafe.ui.activities

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.krepchenko.besafe.R
import com.krepchenko.besafe.db.Safe
import com.krepchenko.besafe.utils.DoubleClickPreventer
import com.krepchenko.besafe.utils.Utils

/**
 * Created by Ann on 04.02.2016.
 */
abstract class InputKeyActivity : BaseKeyActivity() {

    protected var safe: Safe? = null

    protected lateinit var etName: EditText
    protected lateinit var etLogin: EditText
    protected lateinit var etPass: EditText
    protected lateinit var etTel: EditText
    protected lateinit var etExtra: EditText
    private var tilName: TextInputLayout? = null
    private var tilTel: TextInputLayout? = null
    private var tilPass: TextInputLayout? = null

    internal override fun setLayout() {
        setContentView(R.layout.activity_input)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initBack()
    }

    private fun initViews() {
        etName = findViewById(R.id.input_name_et)
        etLogin = findViewById(R.id.input_login_et)
        etPass = findViewById(R.id.input_pass_et)
        etTel = findViewById(R.id.input_tel_et)
        etExtra = findViewById(R.id.input_extra_et)
        tilName = findViewById(R.id.input_name_til)
        tilTel = findViewById(R.id.input_tel_til)
        tilPass = findViewById(R.id.input_pass_til)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_accept) {
            DoubleClickPreventer.onClickWithCustomInterval(123L, { save() }, DoubleClickPreventer.MIN_CLICK_INTERVAL_500_MS)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun save(){
        tilName!!.isErrorEnabled = false
        tilTel!!.isErrorEnabled = false
        tilPass!!.isErrorEnabled = false
        var name = etName.text.toString()
        val login = etLogin.text.toString()
        val password = etPass.text.toString()
        val tel = etTel.text.toString()
        val extra = etExtra.text.toString()
        name = name.trim { it <= ' ' }
        etName.setText(name)
        if (checkName(name) && checkNotEmpty(login, password, tel) && checkPass(password) && checkTel(tel)) {
            setContentValuesToDB(name, login, password, tel, extra)
        }
    }

    private fun checkName(name: String): Boolean {
        if (name.isEmpty()) {
            setTilError(tilName!!, R.string.error_name_empty)
            return false
        } else if (!Utils.checkNameForStartSpecSymbols(name)) {
            setTilError(tilName!!, R.string.error_name_symbols)
            return false
        }
        return true

        /*FirebaseFirestore.getInstance().collection(SafeEntity.TABLE_NAME)
                .whereEqualTo(SafeEntity.EMAIL, GoogleSignIn.getLastSignedInAccount(this)!!.email)
                .whereEqualTo(SafeEntity.NAME, name)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        return if (!task.result.isEmpty) {
                            setTilError(tilName!!, R.string.error_name_exist)
                            false

                        } else {
                            true

                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.exception)
                    }
                }*/
    }

    private fun checkNotEmpty(login: String, pass: String, tel: String): Boolean {
        if (login.isEmpty() && pass.isEmpty() && tel.isEmpty()) {
            dialogFillFields()
            return false
        }
        return true
    }

    private fun checkPass(password: String): Boolean {
        if (!password.isEmpty() && password.length < 4) {
            setTilError(tilPass!!, R.string.error_pass)
            return false
        }
        return true
    }

    private fun checkTel(tel: String): Boolean {
        if (!tel.isEmpty() && !Utils.checkTelNumberWithRegExp(tel)) {
            setTilError(tilTel!!, R.string.error_tel)
            return false
        }
        return true
    }


    private fun setTilError(til: TextInputLayout, id: Int) {
        til.isErrorEnabled = true
        til.error = getString(id)
        til.requestFocus()
    }

    private fun dialogFillFields() {
        val builder = AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle)
        builder.setMessage(getString(R.string.only_login))
        builder.setPositiveButton(R.string.alert_btn_ok, null)
        builder.show()
    }

    internal abstract fun setContentValuesToDB(name: String, login: String, password: String, tel: String, extra: String)
}
