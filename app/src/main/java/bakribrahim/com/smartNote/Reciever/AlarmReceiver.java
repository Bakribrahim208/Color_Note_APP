package bakribrahim.com.smartNote.Reciever;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.models.checklistmodel;
import bakribrahim.com.smartNote.database.Repoistary;
import bakribrahim.com.smartNote.ui.addNote;
import bakribrahim.com.smartNote.ui.add_checklistActicity;
import bakribrahim.com.smartNote.util.util;

import static android.content.Context.NOTIFICATION_SERVICE;
import android.preference.PreferenceManager;



public class AlarmReceiver extends BroadcastReceiver {
    List<checklistmodel>checklistmodels =new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
         NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Bundle bundle = intent.getBundleExtra(util.bakr_Note_Bundle);
        if (bundle != null) {
            int id = bundle.getInt(util.bakr_note_ID);
            Note note = (Note) bundle.getSerializable("notte");
            Repoistary    repoistary =new Repoistary(  context, util.Allnotes);

            Toast.makeText(context, note.getTitle(), Toast.LENGTH_SHORT).show();
            note.setReminder(util.Reminder_null);
             repoistary.update(note);
            NotificationCompat.Builder notif =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_stat_event_note)
                                .setContentText(note.getDescription())
                                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory("remindera" )
                                .setGroup("remindera")
                                .setColor(Color.argb(255, 32, 128, 200));

                if (!TextUtils.isEmpty(note.getTitle())) {
                    notif.setContentTitle(note.getTitle());
                } else {
                    notif.setContentTitle(util.Note_APP);
                }

                notif.setStyle(new NotificationCompat.BigTextStyle().bigText(note.getDescription()).setSummaryText(context.getResources().getString(R.string.remider)));
                // Sets an ID for the notification
                Log.d("NOTIFICATION ID", String.valueOf(id));

                Class c;
                if (note.isIschecklist())
                    c = add_checklistActicity.class;

                else
                     c = addNote.class;

                Intent resultIntent = new Intent(context, c);
                resultIntent.putExtra(util.Note_Extra, note);
                 resultIntent.setAction("REMINDER_NOTE_" + id);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(c);
                // Adds the Intent to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                // Gets a PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                notif.setContentIntent(resultPendingIntent);

                notif.setAutoCancel(true);
//         notif.setDeleteIntent(PendingIntent.getBroadcast(context, id, new Intent(context,
//             BootReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
                   notif.setChannelId("reminders_channel");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

             if (preferences.getBoolean(util.Setting_alarmsound, false))
            notif.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));

            if (preferences.getBoolean(util.Setting_alarmVibrate, false))
                notif.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000, 1000});

            if (preferences.getBoolean(util.Setting_alarmLed, false))
                notif.setLights(Color.argb(255, 32, 128, 200), 1000, 1000);

                // Builds the notification and issues it.
                mNotifyMgr.notify(id, notif.build());
            }
        }




}

