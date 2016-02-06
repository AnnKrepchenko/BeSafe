package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.krepchenko.besafe.R;
import com.krepchenko.besafe.core.SafeApplication;
import com.krepchenko.besafe.db.SafeEntity;

/**
 * Created by Ann on 18.10.2015.
 */
public class ViewActivity extends BaseActivity {

    private static final String KEY_ID = "id";
    private String id;
    private String name;
    private String selection = SafeEntity._ID + "=?";
    private String[] selectionArgs;

    public static void launch(Activity activity, String name) {
        Intent intent = new Intent(activity, ViewActivity.class);
        intent.putExtra(KEY_ID, name);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getIntent().getStringExtra(KEY_ID);
        selectionArgs = new String[]{id};
        initBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContent();
    }

    @Override
    void setLayout() {
        setContentView(R.layout.activity_view);
    }

    private void setContent() {
        Cursor cursor = getContentResolver().query(SafeEntity.CONTENT_URI, null, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(SafeEntity.NAME));
            ((TextView) findViewById(R.id.view_name_tv)).setText(name);
            ((TextView) findViewById(R.id.view_login_tv)).setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.LOGIN))));
            ((TextView) findViewById(R.id.view_pass_tv)).setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.PASS))));
            ((TextView) findViewById(R.id.view_tel_tv)).setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.TEL))));
            final TextView tvExtra = ((TextView) findViewById(R.id.view_extra_tv));
            tvExtra.setText(cryptManager.decrypt(cursor.getString(cursor.getColumnIndex(SafeEntity.EXTRA_INFORMATION))));
            tvExtra.setMovementMethod(new ScrollingMovementMethod());

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_edit:
                EditActivity.launch(this, id);
                return true;
            case R.id.action_delete:
                deleteAction();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAction(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        builder.setMessage(getString(R.string.alert_delete) + " " + name + "?");
        builder.setPositiveButton(R.string.alert_btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContentResolver().delete(SafeEntity.CONTENT_URI, selection, selectionArgs);
                finish();
            }
        });
        builder.setNegativeButton(R.string.alert_btn_cancel, null);
        builder.show();
    }
}
