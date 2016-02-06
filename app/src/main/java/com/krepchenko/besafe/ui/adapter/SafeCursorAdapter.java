package com.krepchenko.besafe.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.krepchenko.besafe.R;
import com.krepchenko.besafe.db.SafeEntity;

/**
 * Created by Ann on 18.10.2015.
 */
public class SafeCursorAdapter extends CursorAdapter {

    private class ViewHolder {
        TextView tv_name;
    }

    private final LayoutInflater inflater;

    public SafeCursorAdapter(Context context) {
        super(context, null, true);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int textColumn = cursor.getColumnIndex(SafeEntity.NAME);
        holder.tv_name.setText(cursor.getString(textColumn));
    }

    @Override
    public View newView(Context context, final Cursor cursor, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        View result;
        result = inflater.inflate(R.layout.item_safe, parent, false);
        holder.tv_name = (TextView) result.findViewById(R.id.item_name);
        result.setTag(holder);
        return result;
    }
}
