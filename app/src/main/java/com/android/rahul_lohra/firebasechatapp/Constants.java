package com.android.rahul_lohra.firebasechatapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.android.rahul_lohra.firebasechatapp.activities.ChatActivity;
import com.android.rahul_lohra.firebasechatapp.activities.ChatRoomActivity;
import com.google.firebase.database.DataSnapshot;

import java.util.Iterator;

/**
 * Created by rkrde on 13-01-2017.
 */

public class Constants {
    public final static String TAG = Constants.class.getSimpleName();
    public final static String SP_NAME = "my_shared_prefs";
    public final static String MESSAGE_KEY = "message_key";
    public final static String USER_NAME_KEY = "username_key";
    public final static String ROOM_NAME_KEY = "roomname_Key";
    final static String GROUP_KEY_MESSAGES = "group_key_messages";



    public static void sendNotification(Context context, DataSnapshot dataSnapshot) {
        Log.d(TAG, "sendNotification");
        Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()) {
            int mId = 1;
            String chat_msg = (String) ((DataSnapshot) iterator.next()).getValue();
            String chat_user_name = (String) ((DataSnapshot) iterator.next()).getValue();

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(chat_user_name)
                            .setGroup(GROUP_KEY_MESSAGES)
//                            .setStyle(new NotificationCompat.InboxStyle()
//                            .setBigContentTitle(chat_user_name)
//                            .setSummaryText(chat_user_name))
//                            .setGroupSummary(true)
                            .setAutoCancel(true)
                            .setContentText(chat_msg);

// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(context, ChatActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(ChatActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(mId, mBuilder.build());
        }
    }

}
