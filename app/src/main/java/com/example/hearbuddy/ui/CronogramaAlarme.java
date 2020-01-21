package com.example.hearbuddy.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.hearbuddy.R;


public class CronogramaAlarme extends BroadcastReceiver {

    public static MediaPlayer player;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        String name=intent.getStringExtra("TODO");
        String date=intent.getStringExtra("DATE");
        String time=intent.getStringExtra("TIME");

        long id=intent.getLongExtra("ID",0);
        Intent i = new Intent(context, CronogramaDisciplina.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(context, (int)id, i, 0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle("Lembrete");
        builder.setContentText(name);
        builder.setSmallIcon(R.drawable.ic_access_alarm_black_24dp);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify((int)id, notification);

        Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);




    }
}
