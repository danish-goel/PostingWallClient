package com.example.postingwall;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * Created by Danish Goel on 20-Apr-15.
 */
public class GcmIntentService extends IntentService
{
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    private final static String TAG = "GcmIntentService";

        public GcmIntentService()
        {
            super("GcmIntentService");
        }

        @Override
        protected void onHandleIntent(Intent intent)
        {
            Bundle extras = intent.getExtras();
            Log.d(TAG, "Notification Data Json :" + extras.getString("message"));

            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
            String messageType = gcm.getMessageType(intent);
            if (!extras.isEmpty())
            {
                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
                {
                    sendNotification("Send error: " + extras.toString());
                }
                else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType))
                {
                    sendNotification("Deleted messages on server: "+ extras.toString());          // If it's a regular GCM message, do some work.
                }
                else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
                {
                    Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                    sendNotification(extras.getString("message"));
                }
            }
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }


        // Put the message into a notification and post it.
        // This is just one simple example of what you might choose to do with
        // a GCM message.
        private void sendNotification(String msg)
        {
            mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,GPSCheck.class), 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                                                   .setSmallIcon(R.drawable.ic_launcher)
                                                   .setContentTitle("New Post")
                                                   .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                                                   .setContentText(msg)
                                                   .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        }
}
