package com.krepchenko.besafe.ui.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.krepchenko.besafe.R;
import com.krepchenko.besafe.core.SafeApplication;
import com.krepchenko.besafe.core.SharedManager;
import com.krepchenko.besafe.crypt.CryptManager;
import com.krepchenko.besafe.db.SafeEntity;

/**
 * Created by Ann on 12.01.2016.
 */
public abstract class BaseActivity  extends AppCompatActivity {

    private Toolbar toolbar;
    protected CryptManager cryptManager;
    protected SharedManager sharedManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout();
        initToolbar();
        cryptManager = ((SafeApplication)getApplicationContext()).getCryptManager();
        sharedManager = ((SafeApplication)getApplicationContext()).getSharedManager();
    }

    abstract void setLayout();

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void initBack(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
