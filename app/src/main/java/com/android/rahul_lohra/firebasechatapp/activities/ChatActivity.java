package com.android.rahul_lohra.firebasechatapp.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.rahul_lohra.firebasechatapp.Constants;
import com.android.rahul_lohra.firebasechatapp.KeysTable;
import com.android.rahul_lohra.firebasechatapp.MyServices.SampleService;
import com.android.rahul_lohra.firebasechatapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatActivity  extends AppCompatActivity implements ChildEventListener{

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_conversation;

    private String user_name,room_name;
    private DatabaseReference root ;
    private String temp_key;

    SampleService mService;
    boolean mBound = false;
    SharedPreferences sp ;
    KeysTable keysTable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        keysTable = new KeysTable(getApplicationContext());

        sp  = getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        btn_send_msg = (Button) findViewById(R.id.btn_send);
        input_msg = (EditText) findViewById(R.id.msg_input);
        chat_conversation = (TextView) findViewById(R.id.textView);

        user_name = sp.getString(Constants.USER_NAME_KEY,null);
        room_name = sp.getString(Constants.ROOM_NAME_KEY,null);

        setTitle(" Room - "+room_name);

        root = FirebaseDatabase.getInstance().getReference().child(room_name);
//        if(mService!=null)
//            mService.hello(root);


        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String,Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String,Object> map2 = new HashMap<String, Object>();
                map2.put("name",user_name);
                map2.put("msg",input_msg.getText().toString());

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, SampleService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        root.removeEventListener(this);
        // Unbind from the service
        if (mBound) {
            mService.addChatListener();
            unbindService(mConnection);
            mBound = false;
        }
    }
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            SampleService.LocalBinder binder = (SampleService.LocalBinder) service;
            mService = binder.getService();
            mService.hello(root);
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private String chat_msg,chat_user_name;

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();
        String key = dataSnapshot.getKey();
//        sp.edit().putString(Constants.MESSAGE_KEY,key).apply();
        keysTable.addKeys(key);

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            chat_conversation.append(chat_user_name +" : "+chat_msg +" \n");
        }


    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        append_chat_conversation(dataSnapshot);
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        append_chat_conversation(dataSnapshot);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,ChatRoomActivity.class));
        finish();
    }
}