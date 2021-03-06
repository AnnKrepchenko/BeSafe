package com.krepchenko.besafe.db;


import android.net.Uri;


public interface SafeEntity {

    String AUTHORITY = "com.krepchenko.besafe.provider";

    String TABLE_NAME = "safes";

    Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    String _ID = "_id";
    String NAME = "name";
    String PASS = "pass";
    String LOGIN = "login";
    String TEL = "tel";
    String EXTRA_INFORMATION = "extraInfo";
    String SECRET_FIELD = "secret_field";
    String EMAIL = "email";

    String CREATE_SCRIPT = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY," +
            NAME + " TEXT NOT NULL," +
            PASS + " TEXT NOT NULL," +
            LOGIN + " TEXT NOT NULL," +
            TEL + " TEXT," +
            EXTRA_INFORMATION + " TEXT," +
            SECRET_FIELD + " TEXT NOT NULL)";

}
