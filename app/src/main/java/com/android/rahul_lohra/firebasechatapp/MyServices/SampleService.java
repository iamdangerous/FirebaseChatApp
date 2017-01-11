package com.android.rahul_lohra.firebasechatapp.MyServices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by rkrde on 11-01-2017.
 */

public class SampleService extends Service implements ChildEventListener {

    DatabaseReference root;
    private final String TAG = SampleService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG,"11");
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG,"22");
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public class LocalBinder extends Binder {
        public SampleService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SampleService.this;
        }
    }

    public void hello(DatabaseReference root){
        this.root = root;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
