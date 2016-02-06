package com.krepchenko.besafe.ui.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.krepchenko.besafe.R;
import com.krepchenko.besafe.core.SafeApplication;
import com.krepchenko.besafe.db.SafeEntity;
import com.krepchenko.besafe.ui.adapter.SafeCursorAdapter;

public class MainActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener, View.OnClickListener {

    private static final int LOADER_ID = 0;
    private ListView listView;
    private SafeCursorAdapter adapter;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        getLoaderManager().initLoader(LOADER_ID, Bundle.EMPTY, this);
    }

    @Override
    void setLayout() {
        setContentView(R.layout.activity_main);
    }

    private void initViews() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.launch(MainActivity.this);
            }
        });
        listView = (ListView) findViewById(R.id.main_listview);
        View emptyView = getLayoutInflater().inflate(R.layout.view_empty, null);
        emptyView.findViewById(R.id.empty_link).setOnClickListener(this);
        addContentView(emptyView, listView.getLayoutParams());
        listView.setEmptyView(emptyView);
        listView.setOnItemClickListener(this);
        initAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(LOADER_ID, Bundle.EMPTY, this);
    }

    private void initAdapter() {
        adapter = new SafeCursorAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.empty_link:
                ((SafeApplication)getApplicationContext()).cleanCryptManaget();
                PinActivity.launch(this);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listView.getAdapter().getItem(position);
        ViewActivity.launch(this, String.valueOf(id));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LOADER_ID:
                final String selection = SafeEntity.SECRET_FIELD + "=?";
                final String[] selectionArgs = new String[]{cryptManager.getSecretField()};
                return new CursorLoader(
                        this,
                        SafeEntity.CONTENT_URI,
                        null,
                        selection,
                        selectionArgs,
                        SafeEntity.NAME + " ASC"
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }


}
