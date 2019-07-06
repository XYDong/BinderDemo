package com.xyd.binderdemo;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import androidx.annotation.RequiresApi;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class BookManagerService extends Service {

    private static final String TAG = "BookManagerService";

    public AtomicBoolean mIsServiceDestoryed = new AtomicBoolean(false);

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mIOnNewBookArrivedListeners = new RemoteCallbackList<>();

    private Binder mBinder = new IBookManager.Stub() {

        @Override
        public List<Book> getBookList() throws RemoteException {
//            SystemClock.sleep(5000);
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            if (!mBookList.contains(book)) {
                mBookList.add(book);
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mIOnNewBookArrivedListeners.register(listener);
//            Log.d(TAG,"registerListener, size:" + mIOnNewBookArrivedListeners.beginBroadcast());
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mIOnNewBookArrivedListeners.unregister(listener);
//            Log.d(TAG,"unregisterListener, size:" + mIOnNewBookArrivedListeners.beginBroadcast());
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        int orSelfPermission = checkCallingOrSelfPermission("com.xyd.binderdemo.permission.ACCESS_BOOK_SERVICE");
        if (orSelfPermission == PackageManager.PERMISSION_DENIED) {
            Log.e(TAG,"您没有访问权限！！！");
            return null;
        }
        if (mBinder == null) {
            throw new UnsupportedOperationException("Not yet implemented");
        }else {
            return mBinder;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Book book1 = new Book(1,"《坚强毅力忍传》","自来也");
        Book book2 = new Book(2,"《Mysql从删库到跑路》","轶民");
        mBookList.add(book1);
        mBookList.add(book2);
        new Thread(new ServiceWorker()).start();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed.set(true);
        super.onDestroy();
    }


    public class ServiceWorker implements Runnable {

        int nameCode = 0;
        @Override
        public void run() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Book book = new Book();
            book.setName("《上甘岭》" + nameCode);
            book.setAuthor("甘龙" + nameCode);
            nameCode ++;
            try {
                onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void onNewBookArrived(Book book) throws RemoteException {
        mBookList.add(book);
        int N = mIOnNewBookArrivedListeners.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IOnNewBookArrivedListener iOnNewBookArrivedListener = mIOnNewBookArrivedListeners.getBroadcastItem(i);
            if (iOnNewBookArrivedListener != null) {
                try {
                    Log.d(TAG,"onNewBookArrived, notify listener:" + iOnNewBookArrivedListener);
                    iOnNewBookArrivedListener.onNewBookArrived(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mIOnNewBookArrivedListeners.finishBroadcast();
    }
}
