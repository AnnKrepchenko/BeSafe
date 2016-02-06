package com.krepchenko.besafe.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class SafeContentProvider extends ContentProvider {

	private DbHelper dbHelper;
	private static final int SAFES = 1;
	private static final int SAFES_SAFE = 2;

	private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		URI_MATCHER.addURI(SafeEntity.AUTHORITY, SafeEntity.TABLE_NAME, SAFES);
		URI_MATCHER.addURI(SafeEntity.AUTHORITY, SafeEntity.TABLE_NAME + "/#", SAFES_SAFE);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DbHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String selectionToAppend = "";
		String tableName = "";
		switch (URI_MATCHER.match(uri)) {
		case SAFES:
			tableName = SafeEntity.TABLE_NAME;
		break;
		case SAFES_SAFE:
			tableName = SafeEntity.TABLE_NAME;
			selectionToAppend = SafeEntity._ID + " = " + uri.getLastPathSegment();
		break;
		default:
			throw new IllegalArgumentException("Wrong uri");
		}
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor c = db.query(tableName, projection, appendSelections(selection, selectionToAppend), selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	
	@Override
	public String getType(Uri uri) {
		String result;
		switch (URI_MATCHER.match(uri)) {
		case SAFES:
			result = "vnd.android.cursor.dir/com.krepchenko.besafe.provider." + SafeEntity.TABLE_NAME;
			break;
		case SAFES_SAFE:
			result = "vnd.android.cursor.item/com.krepchenko.besafe.provider." + SafeEntity.TABLE_NAME;
			break;

		default:
			throw new IllegalArgumentException();
		}
		return result;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long result;
		String tableName;
		switch (URI_MATCHER.match(uri)) {
		case SAFES:
			tableName = SafeEntity.TABLE_NAME;
			break;
		default:
			throw new IllegalArgumentException();
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		result = db.insert(tableName, null, values);
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.withAppendedPath(uri, String.valueOf(result));
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		String tableName;
		String selectionToAppend = "";
		switch (URI_MATCHER.match(uri)) {
		case SAFES:
			tableName = SafeEntity.TABLE_NAME;
			break;
		case SAFES_SAFE:
			tableName = SafeEntity.TABLE_NAME;
			selectionToAppend = SafeEntity._ID + " = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException();
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int affected = db.delete(tableName, appendSelections(selection, selectionToAppend), selectionArgs);
		if (affected > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return affected;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		String tableName;
		String selectionToAppend = "";
		switch (URI_MATCHER.match(uri)) {
		case SAFES:
			tableName = SafeEntity.TABLE_NAME;
			break;
		case SAFES_SAFE:
			tableName = SafeEntity.TABLE_NAME;
			selectionToAppend = SafeEntity._ID + " = " + uri.getLastPathSegment();
			break;
		default:
			throw new IllegalArgumentException();
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int affected = db.update(tableName, values, appendSelections(selection, selectionToAppend), selectionArgs);
		if (affected > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return affected;
	}

	private static String appendSelections(String baseSelection, String selectionToAppend) {
		if (!TextUtils.isEmpty(selectionToAppend)) {
			if (!TextUtils.isEmpty(baseSelection)) {
				baseSelection = " ( " + baseSelection + " ) AND " + selectionToAppend;
			} else {
				baseSelection = selectionToAppend;
			}
		}
		return baseSelection;
	}

}
