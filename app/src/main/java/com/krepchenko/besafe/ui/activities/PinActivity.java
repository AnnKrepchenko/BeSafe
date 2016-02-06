package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.krepchenko.besafe.R;
import com.krepchenko.besafe.core.SafeApplication;

/**
 * Created by Ann on 19.10.2015.
 */
public class PinActivity extends AppCompatActivity implements  TextView.OnEditorActionListener{

    private EditText etPass;
    private SafeApplication application;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, PinActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (SafeApplication)getApplicationContext();
        setContentView(R.layout.activity_pin);
        initViews();
    }

    private void initViews(){
        etPass = (EditText)findViewById(R.id.pin_pass_et);
        etPass.setOnEditorActionListener(this);
        if(!application.getSharedManager().isHintViewed()){
            findViewById(R.id.pin_user_hint).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if(v.getId() == R.id.pin_pass_et) {
                if(etPass.getText().length()!=4) {
                    etPass.setError(getString(R.string.error_pin));
                }
                else {
                    ((SafeApplication) getApplicationContext()).setCryptManager(etPass.getText().toString());
                    ((SafeApplication)getApplicationContext()).getSharedManager().setHintViewed();
                    MainActivity.launch(this);
                }
            }
        }
        return false;
    }
}
