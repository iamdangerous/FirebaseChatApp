package com.android.rahul_lohra.firebasechatapp.MyServices;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.rahul_lohra.firebasechatapp.Constants;
import com.android.rahul_lohra.firebasechatapp.KeysTable;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Iterator;

/**
 * Created by rkrde on 11-01-2017.
 */

public class SampleService extends Service implements ChildEventListener {

    DatabaseReference root;
    private final String TAG = SampleService.class.getSimpleName();
    private final IBinder mBinder = new LocalBinder();
    KeysTable keysTable;
    SharedPreferences sp ;

    private boolean first_match = false;

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "11");
        String key = dataSnapshot.getKey();
        /*
        check weather key is present or not
        if not send notification
         */
        if(!keysTable.keyAlreadyPresent(key)){
            Constants.sendNotification(getApplicationContext(),dataSnapshot);
            keysTable.addKeys(key);
        }else {
            Log.d(TAG,"Key is present");
        }
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.d(TAG, "22");
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

    public void hello(DatabaseReference root) {
        Log.d(TAG, "hello");
        this.root = root;
        removeChatListener();
    }

    public void removeChatListener(){
        root.removeEventListener(this);
    }
    /*
    Will be called when activity calls onStop
     */
    public void addChatListener(){
        root.addChildEventListener(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        keysTable = new KeysTable(getApplicationContext());
        sp = getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onSartCommand");
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return mBinder;
    }
}
