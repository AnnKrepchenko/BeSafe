package com.krepchenko.besafe.ui.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.krepchenko.besafe.crypt.CryptManager
import com.krepchenko.besafe.db.Safe
import com.krepchenko.besafe.db.SafeEntity

import java.util.HashMap

/**
 * Created by Ann on 18.10.2015.
 */
class AddKeyActivity : InputKeyActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.hasExtra(BaseKeyActivity.KEY_PASS))
            encryptedPass = intent.getStringExtra(BaseKeyActivity.KEY_PASS)
    }

    override fun setContentValuesToDB(name: String, login: String, password: String, tel: String, extra: String) {

        val user = HashMap<String, Any>()
        user[SafeEntity.NAME] = name
        user[SafeEntity.LOGIN] = CryptManager.encrypt(encryptedPass, login)
        user[SafeEntity.PASS] = CryptManager.encrypt(encryptedPass, password)
        user[SafeEntity.TEL] = CryptManager.encrypt(encryptedPass, tel)
        user[SafeEntity.EXTRA_INFORMATION] = CryptManager.encrypt(encryptedPass, extra)
        user[SafeEntity.SECRET_FIELD] = CryptManager.getSecretField(encryptedPass)
        user[SafeEntity.EMAIL] = GoogleSignIn.getLastSignedInAccount(this)!!.email?:""

        FirebaseFirestore.getInstance().collection(SafeEntity.TABLE_NAME)
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.id)
                    MainKeyActivity.launch(this,encryptedPass)
                    finish()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    companion object {

        fun launch(activity: Activity, pass: String) {
            val intent = Intent(activity, AddKeyActivity::class.java)
            intent.putExtra(BaseKeyActivity.KEY_PASS, pass)
            activity.startActivity(intent)
        }
    }
}
