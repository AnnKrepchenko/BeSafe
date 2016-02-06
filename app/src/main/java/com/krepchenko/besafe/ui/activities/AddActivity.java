package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.krepchenko.besafe.db.SafeEntity;

/**
 * Created by Ann on 18.10.2015.
 */
public class AddActivity extends InputActivity {



    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AddActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    void getName() {
        selectionName = SafeEntity.NAME + "=? AND " + SafeEntity.SECRET_FIELD + "=?";
        selectionArgsName =  new String[]{ etName.getText().toString(),cryptManager.getSecretField()};
    }

    @Override
    void setContentValuesToDB(String name, String login, String password, String tel, String extra) {
        ContentValues safe = new ContentValues();
        safe.put(SafeEntity.NAME, name);
        safe.put(SafeEntity.LOGIN, cryptManager.encrypt(login));
        safe.put(SafeEntity.PASS, cryptManager.encrypt(password));
        safe.put(SafeEntity.TEL, cryptManager.encrypt(tel));
        safe.put(SafeEntity.EXTRA_INFORMATION, cryptManager.encrypt(extra));
        safe.put(SafeEntity.SECRET_FIELD,cryptManager.getSecretField());
        getContentResolver().insert(SafeEntity.CONTENT_URI, safe);
        finish();
    }
}
