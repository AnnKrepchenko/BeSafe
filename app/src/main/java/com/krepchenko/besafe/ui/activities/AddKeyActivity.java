package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.krepchenko.besafe.crypt.CryptManager;
import com.krepchenko.besafe.db.Safe;
import com.krepchenko.besafe.db.SafeEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ann on 18.10.2015.
 */
public class AddKeyActivity extends InputKeyActivity {

    private String TAG = this.getClass().getCanonicalName();

    public static void launch(Activity activity, String pass) {
        Intent intent = new Intent(activity, AddKeyActivity.class);
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

        Map<String, Object> user = new HashMap<>();
        user.put(SafeEntity.NAME, name);
        user.put(SafeEntity.LOGIN, CryptManager.encrypt(encryptedPass, login));
        user.put(SafeEntity.PASS, CryptManager.encrypt(encryptedPass, password));
        user.put(SafeEntity.TEL, CryptManager.encrypt(encryptedPass, tel));
        user.put(SafeEntity.EXTRA_INFORMATION, CryptManager.encrypt(encryptedPass, extra));
        user.put(SafeEntity.SECRET_FIELD, CryptManager.getSecretField(encryptedPass));
        user.put(SafeEntity.EMAIL, GoogleSignIn.getLastSignedInAccount(this).getEmail());

        FirebaseFirestore.getInstance().collection(SafeEntity.TABLE_NAME)
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        MainKeyActivity.Companion.launch(this,encryptedPass);
        finish();
    }
}
