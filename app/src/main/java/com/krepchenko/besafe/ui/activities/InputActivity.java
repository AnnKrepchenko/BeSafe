package com.krepchenko.besafe.ui.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.krepchenko.besafe.R;
import com.krepchenko.besafe.db.SafeEntity;
import com.krepchenko.besafe.utils.Utils;

/**
 * Created by Ann on 04.02.2016.
 */
public abstract class InputActivity extends BaseActivity {

    protected String selectionName;
    protected String[] selectionArgsName;

    protected EditText etName;
    protected EditText etLogin;
    protected EditText etPass;
    protected EditText etTel;
    protected EditText etExtra;
    private TextInputLayout tilName;
    private TextInputLayout tilTel;
    private TextInputLayout tilPass;

    @Override
    void setLayout() {
        setContentView(R.layout.activity_input);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initBack();
    }

    private void initViews() {
        etName = (EditText) findViewById(R.id.input_name_et);
        etLogin = (EditText) findViewById(R.id.input_login_et);
        etPass = (EditText) findViewById(R.id.input_pass_et);
        etTel = (EditText) findViewById(R.id.input_tel_et);
        etExtra = (EditText) findViewById(R.id.input_extra_et);
        tilName = (TextInputLayout) findViewById(R.id.input_name_til);
        tilTel = (TextInputLayout) findViewById(R.id.input_tel_til);
        tilPass = (TextInputLayout) findViewById(R.id.input_pass_til);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_accept) {
            tilName.setErrorEnabled(false);
            tilTel.setErrorEnabled(false);
            tilPass.setErrorEnabled(false);
            String name = etName.getText().toString();
            final String login = etLogin.getText().toString();
            final String password = etPass.getText().toString();
            final String tel = etTel.getText().toString();
            final String extra = etExtra.getText().toString();
            name = name.trim();
            etName.setText(name);
            if (checkName(name) && checkNotEmpty(login, password, tel) && checkPass(password) && checkTel(tel)) {
                setContentValuesToDB(name, login, password, tel, extra);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean checkName(String name) {
        if (name.isEmpty()) {
            setTilError(tilName, R.string.error_name_empty);
            return false;
        } else if (!Utils.checkNameForStartSpecSymbols(name)) {
            setTilError(tilName, R.string.error_name_symbols);
            return false;
        }
        getName();
        Cursor cursor = getContentResolver().query(SafeEntity.CONTENT_URI, null, selectionName, selectionArgsName, null);
        if (cursor.moveToFirst()) {
            setTilError(tilName, R.string.error_name_exist);
            return false;
        }
        return true;
    }

    private boolean checkNotEmpty(String login, String pass, String tel) {
        if (login.isEmpty() && pass.isEmpty() && tel.isEmpty()) {
            dialogFillFields();
            return false;
        }
        return true;
    }

    private boolean checkPass(String password) {
        if (!password.isEmpty() && password.length() < 4) {
            setTilError(tilPass, R.string.error_pass);
            return false;
        }
        return true;
    }

    private boolean checkTel(String tel) {
        if (!tel.isEmpty() && !Utils.checkTelNumberWithRegExp(tel)) {
            setTilError(tilTel, R.string.error_tel);
            return false;
        }
        return true;
    }


    private void setTilError(TextInputLayout til, int id) {
        til.setErrorEnabled(true);
        til.setError(getString(id));
        til.requestFocus();
    }

    abstract void getName();

    private void dialogFillFields() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(getString(R.string.only_login));
        builder.setPositiveButton(R.string.alert_btn_ok, null);
        builder.show();
    }

    abstract void setContentValuesToDB(String name, String login, String password, String tel, String extra);
}
