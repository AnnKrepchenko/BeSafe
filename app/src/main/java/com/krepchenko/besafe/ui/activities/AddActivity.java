package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.krepchenko.besafe.crypt.CryptManager;
import com.krepchenko.besafe.db.SafeEntity;

/**
 * Created by Ann on 18.10.2015.
 */
public class AddActivity extends InputActivity {


    public static void launch(Activity activity, String pass) {
        Intent intent = new Intent(activity, AddActivity.class);
        intent.putExtra(KEY_PASS, pass);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().hasExtra(KEY_PASS))
            encryptedPass = getIntent().getStringExtra(KEY_PASS);
    }

    @Override
    void getName() {
        selectionName = SafeEntity.NAME + "=? AND " + SafeEntity.SECRET_FIELD + "=?";
        selectionArgsName = new String[]{etName.getText().toString(), CryptManager.getSecretField(encryptedPass)};
    }

    @Override
    void setContentValuesToDB(String name, String login, String password, String tel, String extra) {
        ContentValues safe = new ContentValues();
        safe.put(SafeEntity.NAME, name);
        safe.put(SafeEntity.LOGIN, CryptManager.encrypt(encryptedPass, login));
        safe.put(SafeEntity.PASS, CryptManager.encrypt(encryptedPass, password));
        safe.put(SafeEntity.TEL, CryptManager.encrypt(encryptedPass, tel));
        safe.put(SafeEntity.EXTRA_INFORMATION, CryptManager.encrypt(encryptedPass, extra));
        safe.put(SafeEntity.SECRET_FIELD, CryptManager.getSecretField(encryptedPass));
        getContentResolver().insert(SafeEntity.CONTENT_URI, safe);
        MainActivity.launch(this,encryptedPass);
        finish();
    }
}
