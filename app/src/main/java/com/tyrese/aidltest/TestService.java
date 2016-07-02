package com.tyrese.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestService extends Service {

    private RemoteCallbackList<IOnNewBookArrivedListener> mListeners = new RemoteCallbackList<>();
    private AtomicBoolean isDistroyed = new AtomicBoolean(false);

    public TestService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Worker()).start();
    }

    @Override
    public void onDestroy() {
        isDistroyed.set(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void test(Book book) throws RemoteException {
            Log.d("Service test", book.getName());
        }

        @Override
        public void printName(Person person) throws RemoteException {
            Log.d("Service printName", person.getName());
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListeners.register(listener);
        }

        @Override
        public void unRegisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mListeners.unregister(listener);
        }
    };

    private void onNewBookArrived(Book book) {
        int count = mListeners.beginBroadcast();
        for (int i = 0; i < count; i ++) {
            IOnNewBookArrivedListener listener = mListeners.getBroadcastItem(i);
            try {
                listener.onNewBookArrived(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mListeners.finishBroadcast();
    }

    private class Worker implements Runnable {
        @Override
        public void run() {
            int count = 0;
            while (!isDistroyed.get()) {
                count ++;
                try {
                    TimeUnit.MILLISECONDS.sleep(3000);
                    Book book = new Book();
                    book.setName("book " + count);
                    onNewBookArrived(book);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
