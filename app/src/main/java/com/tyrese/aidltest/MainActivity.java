package com.tyrese.aidltest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private IRemoteService mRemoteService;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mRemoteService = IRemoteService.Stub.asInterface(service);
            try {
                mRemoteService.registerListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mRemoteService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, TestService.class);
        bindService(intent, sc, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        if (mRemoteService != null && mRemoteService.asBinder().isBinderAlive()) {
            try {
                mRemoteService.unRegisterListener(listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(sc);
        super.onDestroy();
    }

    public void test(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Book book = new Book();
                book.setName("book");
                Person person = new Person();
                person.setName("person name");
                try {
                    mRemoteService.test(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                try {
                    mRemoteService.printName(person);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private IOnNewBookArrivedListener.Stub listener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.d("MainActivity", book.getName());
        }
    };

}
