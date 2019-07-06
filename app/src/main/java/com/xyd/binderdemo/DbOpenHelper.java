package com.xyd.binderdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * @ProjectName: BinderDemo
 * @Package: com.xyd.binderdemo
 * @ClassName: DbOpenHelper
 * @Description: java类作用描述
 * @Author: 作者名
 * @CreateDate: 2019-06-30 18:51
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019-06-30 18:51
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "book_provider.db";
    public static final String BOOK_TABLE_NAME = "book";
    public static final String USER_TABLE_NAME = "user";

    public static final int DB_VIERSION = 1;

    public static final String CREATE_BOOK_TABLE = "create table if not exists " + BOOK_TABLE_NAME + "(_id integer primary key," + "name text)";
    public static final String CREATE_USER_TABLE = "create table if not exists " + USER_TABLE_NAME + "(_id integer primary key," + "name text)";

    public DbOpenHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VIERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BOOK_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
