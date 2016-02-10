package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.krepchenko.besafe.crypt.CryptManager;
import com.krepchenko.besafe.db.SafeEntity;

/**
 * Created by Ann on 12.01.2016.
 */
public class EditActivity extends InputActivity {


    private static final String KEY_ID = "id";
    private String selection = SafeEntity._ID + "=?";
    private String[] selectionArgs;
    private String id;

    public static void launch(Activity activity, String id, String pass) {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra(KEY_ID, id);
        intent.putExtra(KEY_PASS, pass);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra(KEY_ID);
        if (getIntent().hasExtra(KEY_PASS))
            encryptedPass = getIntent().getStringExtra(KEY_PASS);
        selectionArgs = new String[]{id};
        setContent();
    }

    @Override
    void getName() {
        selectionName = SafeEntity.NAME + "=? AND " + SafeEntity._ID + "<>? AND " + SafeEntity.SECRET_FIELD + "=?";
        selectionArgsName = new String[]{etName.getText().toString(), id, CryptManager.getSecretField(encryptedPass)};
    }

    private void setContent() {
        Cursor cursor = getContentResolver().query(SafeEntity.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndex(SafeEntity.NAME)));
            etLogin.setText(CryptManager.decrypt(encryptedPass, cursor.getString(cursor.getColumnIndex(SafeEntity.LOGIN))));
            etPass.setText(CryptManager.decrypt(encryptedPass, cursor.getString(cursor.getColumnIndex(SafeEntity.PASS))));
            etTel.setText(CryptManager.decrypt(encryptedPass, cursor.getString(cursor.getColumnIndex(SafeEntity.TEL))));
            etExtra.setText(CryptManager.decrypt(encryptedPass, cursor.getString(cursor.getColumnIndex(SafeEntity.EXTRA_INFORMATION))));
        }
    }


    @Override
    void setContentValuesToDB(String name, String login, String password, String tel, String extra) {
        ContentValues safe = new ContentValues();
        safe.put(SafeEntity.NAME, name);
        safe.put(SafeEntity.LOGIN, CryptManager.encrypt(encryptedPass, login));
        safe.put(SafeEntity.PASS, CryptManager.encrypt(encryptedPass, password));
        safe.put(SafeEntity.TEL, CryptManager.encrypt(encryptedPass, tel));
        safe.put(SafeEntity.EXTRA_INFORMATION, CryptManager.encrypt(encryptedPass, extra));
        getContentResolver().update(SafeEntity.CONTENT_URI, safe, selection, selectionArgs);
        finish();
    }


}
