package com.krepchenko.besafe.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

public abstract class BaseActivity : AppCompatActivity() {

    protected var TAG = this.javaClass.canonicalName

    protected lateinit var firestore:FirebaseFirestore

}