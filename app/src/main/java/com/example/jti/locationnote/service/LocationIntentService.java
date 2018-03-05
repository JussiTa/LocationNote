package com.example.jti.locationnote.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.jti.locationnote.R;
import com.example.jti.locationnote.datamanagement.HandleFiles;
import com.example.jti.locationnote.datamanagement.TimeManager;
import com.example.jti.locationnote.gui.AlertDetails;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_START = "start";
    private static final String ACTION_UPDATED = "updated";
    private String sound ="alarm";
    private HandleFiles handleFiles;
    private ArrayList<String> noteList;
    private int notifId =0;
    private TimeManager timeManager;

    public LocationIntentService() {
        super("LocationIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                if (noteList==null) {
                    try {
                        noteList = handleFiles.getList();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                Bundle bundle;
                bundle= intent.getExtras();
                //String latitude= bundle.getString("LATITUDE");
                String note= bundle.getString("NOTE");


                notification(note);

            } else if (ACTION_UPDATED.equals(action)) {
                try {
                    noteList=handleFiles.getList();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
    }




    private void notification(String note){
        notifId=1;
        Intent  intent = new Intent(this, AlertDetails.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
         Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"notelist")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Muistutus")
                .setContentText(note)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setSound(uri)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
             notificationManager.notify(notifId,builder.build());
    }



}
