package com.krepchenko.besafe.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestore
import com.krepchenko.besafe.crypt.CryptManager
import com.krepchenko.besafe.db.Safe
import com.krepchenko.besafe.db.SafeEntity

import java.util.HashMap

/**
 * Created by Ann on 12.01.2016.
 */
class EditKeyActivity : InputKeyActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        safe = intent.getParcelableExtra(KEY_SAFE)
        if (intent.hasExtra(BaseKeyActivity.KEY_PASS))
            encryptedPass = intent.getStringExtra(BaseKeyActivity.KEY_PASS)
        setContent()
    }

    private fun setContent() {

        etName.setText(safe!!.name)
        etLogin.setText(CryptManager.decrypt(encryptedPass, safe!!.login))
        etPass.setText(CryptManager.decrypt(encryptedPass, safe!!.pass))
        etTel.setText(CryptManager.decrypt(encryptedPass, safe!!.tel))
        etExtra.setText(CryptManager.decrypt(encryptedPass, safe!!.extraInfo))
    }


    override fun setContentValuesToDB(name: String, login: String, password: String, tel: String, extra: String) {

        val user = HashMap<String, Any>()
        user[SafeEntity.NAME] = name
        user[SafeEntity.LOGIN] = CryptManager.encrypt(encryptedPass, login)
        user[SafeEntity.PASS] = CryptManager.encrypt(encryptedPass, password)
        user[SafeEntity.TEL] = CryptManager.encrypt(encryptedPass, tel)
        user[SafeEntity.EXTRA_INFORMATION] = CryptManager.encrypt(encryptedPass, extra)
        user[SafeEntity.SECRET_FIELD] = CryptManager.getSecretField(encryptedPass)

        FirebaseFirestore.getInstance().collection(SafeEntity.TABLE_NAME).document(safe!!.serverId)
                .update(user)
                .addOnSuccessListener {
                    MainKeyActivity.launch(this,encryptedPass)
                    finish()
                }
                .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    companion object {

        private val KEY_SAFE = "safe"

        fun launch(activity: Activity, safe: Safe, pass: String) {
            val intent = Intent(activity, EditKeyActivity::class.java)
            intent.putExtra(KEY_SAFE, safe)
            intent.putExtra(BaseKeyActivity.KEY_PASS, pass)
            activity.startActivity(intent)
        }
    }


}
