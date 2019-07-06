package com.xyd.binderdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final int MESSAGE_NEW_BOOK_ARRIVED = 0x10001;

    public IBookManager mRemoteBookManger;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_NEW_BOOK_ARRIVED:
                    Log.d(TAG,"receive new book : " + msg.obj);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"onServiceConnected当前线程名===" + Thread.currentThread().getName());
            IBookManager iBookManager = IBookManager.Stub.asInterface(service);
            mRemoteBookManger = iBookManager;
            try {
                List<Book> bookList = iBookManager.getBookList();
                Log.i(TAG,"query book list, list type : " + bookList.getClass().getCanonicalName());
                Log.i(TAG,"query book list : " + bookList.toString());
                Book book = new Book(3,"《艺术探索》", "扔鱼缸");
                iBookManager.addBook(book);
                Log.i(TAG,"query book list, list type : " + bookList.getClass().getCanonicalName());
                Log.i(TAG,"query book list : " + bookList.toString());
                iBookManager.registerListener(newBookArrivedListener);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"onServiceDisconnected当前线程名===" + Thread.currentThread().getName());
            mRemoteBookManger = null;
            Log.d(TAG,"binder died!");
        }
    };

    private IOnNewBookArrivedListener newBookArrivedListener = new IOnNewBookArrivedListener.Stub(){

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            mHandler.obtainMessage(MESSAGE_NEW_BOOK_ARRIVED,book.toString()).sendToTarget();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this,BookManagerService.class);
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
        findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                try {
//                    List<Book> bookList = mRemoteBookManger.getBookList();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
                startActivity(new Intent(MainActivity.this,ProviderActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mRemoteBookManger != null && mRemoteBookManger.asBinder().isBinderAlive()) {
            try {
                Log.i(TAG,"unregister listener:" + newBookArrivedListener);
                mRemoteBookManger.unRegisterListener(newBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(serviceConnection);
        super.onDestroy();
    }

}
