package com.krepchenko.besafe.ui.activities

import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Intent
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.krepchenko.besafe.R
import com.krepchenko.besafe.db.Safe
import com.krepchenko.besafe.db.SafeEntity
import com.krepchenko.besafe.utils.CustomConsumer
import com.krepchenko.besafe.utils.toast
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : BaseActivity(),LoaderManager.LoaderCallbacks<Cursor>  {

    private val RC_SIGN_IN = 1001
    private val LOADER_ID = 0


    private var googleApiClient: GoogleApiClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sign_in_button.setOnClickListener { signInGoogle() }
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        signedIn(account)
        loaderManager.restartLoader(LOADER_ID, Bundle.EMPTY, this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            signedIn(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            signedIn(null)
        }
    }


    private fun signedIn(googleAcc: GoogleSignInAccount?) {
        if (!TextUtils.isEmpty(googleAcc?.email)) {
            startActivity(Intent(this, PinActivity::class.java))
        }
    }

    private fun signInGoogle() {
        googleApiClient?.let {
            handleSignInRequest()
        } ?: run {
            initGoogleSDK(object : CustomConsumer<Boolean> {
                override fun accept(connected: Boolean) {
                    if (connected!!) {
                        handleSignInRequest()
                    } else {
                        toast(R.string.google_connection_failed)
                    }
                }
            })
        }

    }


    private fun handleSignInRequest() {
        googleApiClient?.let {
            if (googleApiClient!!.isConnected) {
                val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
        }

    }


    private fun initGoogleSDK(connected: CustomConsumer<Boolean>?) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestScopes(
                        Scope(Scopes.EMAIL),
                        Scope(Scopes.PLUS_ME),
                        Scope(Scopes.PROFILE))
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this
                ) {
                    connected?.accept(false)
                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                    override fun onConnected(bundle: Bundle?) {
                        connected?.accept(true)
                    }

                    override fun onConnectionSuspended(i: Int) {

                    }
                })
                .build()
    }

    override fun onDestroy() {
        destroyGoogleSdk()
        super.onDestroy()
    }

    private fun destroyGoogleSdk() {
        googleApiClient?.let {
            if (googleApiClient!!.isConnected) {
                googleApiClient!!.stopAutoManage(this)
                googleApiClient!!.disconnect()
            }
        }

    }


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this@LoginActivity, SafeEntity.CONTENT_URI, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        cursor?.let {
            var safes = mutableListOf<Safe>()
            while (it.moveToNext()) {
                val safe = Safe(it.getString(it.getColumnIndex(SafeEntity.NAME)), it.getString(it.getColumnIndex(SafeEntity.PASS)), it.getString(it.getColumnIndex(SafeEntity.LOGIN)), it.getString(it.getColumnIndex(SafeEntity.TEL)), it.getString(it.getColumnIndex(SafeEntity.EXTRA_INFORMATION)), GoogleSignIn.getLastSignedInAccount(this)!!.email!!)
                safes.add(safe)
            }
            FirebaseFirestore.getInstance().collection(SafeEntity.TABLE_NAME)
                    .add(safes)
                    .addOnSuccessListener {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + it.id)
                    }
                    .addOnFailureListener {
                        Log.w(TAG, "Error adding document", it)
                    }
        }
    }


    override fun onLoaderReset(loader: Loader<Cursor>) {

    }

}