package com.xyd.binderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.net.URI;

/**
 * @ProjectName: BinderDemo
 * @Package: com.xyd.binderdemo
 * @ClassName: BookContentProvider
 * @Description: java类作用描述
 * @Author: 作者名
 * @CreateDate: 2019-06-30 18:06
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019-06-30 18:06
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class BookContentProvider extends ContentProvider {
    public static final String TAG = "BookContentProvider";

    public static final String AUUTHORITY = "com.xyd.binderdemo.book.privider";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://" + AUUTHORITY + "/book" );
    public static final Uri USER_CONTENT_URI = Uri.parse("content://" + AUUTHORITY + "/user" );

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUUTHORITY,"book",BOOK_URI_CODE);
        uriMatcher.addURI(AUUTHORITY,"user",USER_URI_CODE);
    }

    private Context mContext;
    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        Log.d(TAG,"onCreate, current thread : " + Thread.currentThread().getName());
        mContext = getContext();
//        new Thread(){
//            @Override
//            public void run() {
//                initProviderData();
//            }
//        }.start();
        initProviderData();

        return true;
    }

    private void initProviderData() {
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from " + DbOpenHelper.BOOK_TABLE_NAME );
        mDb.execSQL("delete from " + DbOpenHelper.USER_TABLE_NAME );
        mDb.execSQL("insert into book  values(4, 'Android');");
        mDb.execSQL("insert into book  values(5, 'Ios');");
        mDb.execSQL("insert into book  values(6, 'Windows');");
        mDb.execSQL("insert into user  values(3, 'Jake', 1);");
        mDb.execSQL("insert into user  values(4, 'Tom', 0);");
    }

    @androidx.annotation.Nullable
    @Override
    public Cursor query(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable String[] projection, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs, @androidx.annotation.Nullable String sortOrder) {
        Log.d(TAG,"query, current thread : " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
           throw  new IllegalArgumentException("不受支持的URI: " + uri);
        }
        return mDb.query(tableName,projection,selection,selectionArgs,null,sortOrder,null);
    }

    @androidx.annotation.Nullable
    @Override
    public String getType(@androidx.annotation.NonNull Uri uri) {
        Log.d(TAG,"getType, current thread : " + Thread.currentThread().getName());
        return null;
    }

    @androidx.annotation.Nullable
    @Override
    public Uri insert(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values) {
        Log.d(TAG,"insert, current thread : " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw  new IllegalArgumentException("不受支持的URI: " + uri);
        }
        mDb.insert(tableName,null,values);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public int delete(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        Log.d(TAG,"delete, current thread : " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw  new IllegalArgumentException("不受支持的URI: " + uri);
        }
        int delete = mDb.delete(tableName, selection, selectionArgs);
        if (delete > 0) {
            mContext.getContentResolver().notifyChange(uri,null);
        }
        return delete;
    }

    @Override
    public int update(@androidx.annotation.NonNull Uri uri, @androidx.annotation.Nullable ContentValues values, @androidx.annotation.Nullable String selection, @androidx.annotation.Nullable String[] selectionArgs) {
        Log.d(TAG,"update, current thread : " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (TextUtils.isEmpty(tableName)) {
            throw  new IllegalArgumentException("不受支持的URI: " + uri);
        }
        int update = mDb.update(tableName, values, selection, selectionArgs);
        if (update > 0) {
            mContext.getContentResolver().notifyChange(uri,null);
        }
        return update;
    }


    public String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

}
