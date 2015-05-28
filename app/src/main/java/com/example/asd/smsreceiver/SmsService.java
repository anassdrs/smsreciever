package com.example.asd.smsreceiver;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class SmsService extends Service {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void showNotification(String text) {
        String key = getKeyFromString(text);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, Main.class), 0);
        Context context = getApplicationContext();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle("SMSReceiver")
                .setContentText(key)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.abc_ic_clear_mtrl_alpha)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notificationManager.notify(12, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String sms_body = intent.getExtras().getString("sms_body");
        showNotification(sms_body);
        ConfirmTask task = new ConfirmTask();
        task.execute(getKeyFromString(sms_body));
        return START_STICKY;
    }

    public String getKeyFromString(String text){
        String words[] = text.split(" ");
        String key = words[1];
        return key;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public class ConfirmTask extends AsyncTask<String, Void, Void> {
          @Override
        protected Void doInBackground(String... params) {
            String sUrl = "http://vapnik.ru:8000/receive/?key=";
            sUrl = sUrl + params[0];
            try {
                URL url = new URL(sUrl);
                new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (Exception e) {
            }
            return null;
        }
    }
}
