package com.chris.hidedocument.frame;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by zoulx on 2016/3/24.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    public static final String CREATE_SMS = "create table "+MainApplication.smsTableName+" (id integer primary key autoincrement,person text,date text,address text,type text,body text)";
    public static final String CREATE_CALL_MSG="create table "+MainApplication.callMsgTableName+" (id integer primary key autoincrement,person text,date text,address text,type text)";

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SMS);
        db.execSQL(CREATE_CALL_MSG);
        Toast.makeText(mContext, "CREATE_SMS", Toast.LENGTH_SHORT).show();
        Toast.makeText(mContext, "CREATE_CALL_MSG", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
