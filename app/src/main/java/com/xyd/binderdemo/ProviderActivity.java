package com.xyd.binderdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class ProviderActivity extends AppCompatActivity {

    public static final String TAG = "ProviderActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider);
//        final Uri uri = Uri.parse("content://com.xyd.binderdemo.book.provider");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            getContentResolver().query(uri,null,null,null,null,null);
//            getContentResolver().query(uri,null,null,null,null,null);
//            getContentResolver().query(uri,null,null,null,null,null);
//        }

        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri bookUri = Uri.parse("content://com.xyd.binderdemo.book.provider/book");
                ContentValues contentValues = new ContentValues();
                contentValues.put("_id",7);
                contentValues.put("name","十月围城");

                getContentResolver().insert(bookUri, contentValues);
                Log.d(TAG,"数据以插入");
                Cursor query = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
                if (query.moveToNext()) {
                    Book book = new Book();
                    book.setId(query.getInt(0));
                    book.setName(query.getString(1));
                    Log.d(TAG,"查询wancheng" + book.toString());
                }
                query.close();
            }
        });

    }
}
