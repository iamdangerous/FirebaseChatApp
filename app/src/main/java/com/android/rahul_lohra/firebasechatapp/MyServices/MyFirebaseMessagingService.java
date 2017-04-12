package com.android.rahul_lohra.firebasechatapp.MyServices;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by rkrde on 11-01-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String msgId = remoteMessage.getMessageId();
        System.out.println("MessageId:"+msgId);
    }
}
