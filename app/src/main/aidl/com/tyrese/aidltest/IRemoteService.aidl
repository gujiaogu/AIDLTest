// IRemoteService.aidl
package com.tyrese.aidltest;

// Declare any non-default types here with import statements
import com.tyrese.aidltest.Book;
import com.tyrese.aidltest.Person;
import com.tyrese.aidltest.IOnNewBookArrivedListener;
interface IRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    void test(in Book book);

    void printName(in Person person);
    void registerListener(in IOnNewBookArrivedListener listener);
    void unRegisterListener(in IOnNewBookArrivedListener listener);
}
