// IOnNewBookArrivedListener.aidl
package com.tyrese.aidltest;

// Declare any non-default types here with import statements
import com.tyrese.aidltest.Book;
interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
