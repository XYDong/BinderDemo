// IOnNewBookArrivedListener.aidl
package com.xyd.binderdemo;
import com.xyd.binderdemo.Book;

// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
