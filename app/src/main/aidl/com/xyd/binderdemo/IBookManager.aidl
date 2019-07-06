// IBookManager.aidl
package com.xyd.binderdemo;
import com.xyd.binderdemo.Book;
import com.xyd.binderdemo.IOnNewBookArrivedListener;


// Declare any non-default types here with import statements

interface IBookManager {

        List<Book> getBookList();
        void addBook(in Book book);
        void registerListener(IOnNewBookArrivedListener listener);
        void unRegisterListener(IOnNewBookArrivedListener listener);
}
