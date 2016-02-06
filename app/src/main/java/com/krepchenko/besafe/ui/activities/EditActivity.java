package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.krepchenko.besafe.db.SafeEntity;

/**
 * Created by Ann on 12.01.2016.
 */
public class EditActivity extends InputActivity {


    private static final String KEY_ID = "id";
    private String selection = SafeEntity._ID + "=?";
    private String[] selectionArgs;
    private String id;

    public static void launch(Activity activity, String id) {
        Intent intent = new Intent(activity, EditActivity.class);
        intent.putExtra(KEY_ID, id);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra(KEY_ID);
        selectionArgs = new String[]{id};
        setContent();
    }

    @Override
    void getName() {
        selectionName = SafeEntity.NAME + "=? AND " + SafeEntity._ID + "<>? AND " + SafeEntity.SECRET_FIELD + "=?";
        selectionArgsName =  new String[]{ etName.getText().toString(),id,cryptManager.getSecretField()};
    }

    private void setContent() {
        Cursor cursor = getContentResolver().query(SafeEntity.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndex(SafeEntity.NAME)));
            etLogin.setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.LOGIN))));
            etPass.setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.PASS))));
            etTel.setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.TEL))));
            etExtra.setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.EXTRA_INFORMATION))));
        }
    }


    @Override
    void setContentValuesToDB(String name, String login, String password, String tel, String extra) {
        ContentValues safe = new ContentValues();
        safe.put(SafeEntity.NAME, name);
        safe.put(SafeEntity.LOGIN, cryptManager.encrypt(login));
        safe.put(SafeEntity.PASS, cryptManager.encrypt(password));
        safe.put(SafeEntity.TEL, cryptManager.encrypt(tel));
        safe.put(SafeEntity.EXTRA_INFORMATION, cryptManager.encrypt(extra));
        getContentResolver().update(SafeEntity.CONTENT_URI, safe, selection, selectionArgs);
        finish();
    }



}
