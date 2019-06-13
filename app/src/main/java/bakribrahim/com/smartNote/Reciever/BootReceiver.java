package bakribrahim.com.smartNote.Reciever;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.database.Repoistary;
import bakribrahim.com.smartNote.ui.addNotetest;
import bakribrahim.com.smartNote.ui.add_checklistActicity;
import bakribrahim.com.smartNote.util.util;

import static android.content.Context.NOTIFICATION_SERVICE;


public class BootReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
       // ArrayList<NoteObj> list = new NotesDbHelper(context).getNotificationsAndReminders();
        // Gets an instance of the NotificationManager service
        Toast.makeText(context, "BootReceiver", Toast.LENGTH_SHORT).show();
        new notifyAsyncTask(context).execute();
    }


    private static class notifyAsyncTask extends AsyncTask<Void, Void, Void>
    {
        Context context;

        public notifyAsyncTask(Context context)
        {
            this.context=context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("boot", "Notify async started");
            Repoistary repoistary = new Repoistary(context, util.Allnotes);

            List<Note> noteList = repoistary.GetNotifcationAndReminderNotes();
            Log.e("boot", "size" + noteList.size());
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        for (int i = 0; i < noteList.size(); i++) {
            Note note = noteList.get(i);
            int id = note.getID();
            String title = note.getTitle();
            String content = note.getDescription();
            // Sets an ID for the notification
            Log.d("ID", String.valueOf(id));
            Bundle bundle = new Bundle();


            if (!note.getReminder().equals(util.Reminder_null)) {
                Intent resultIntent = new Intent(context, AlarmReceiver.class);
                bundle.putSerializable("notte", note);
                resultIntent.putExtra(util.bakr_Note_Bundle, bundle);

                SimpleDateFormat readableDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(readableDateFormat.parse(note.getReminder()));
                    Log.e(getClass().getName(), "Current time: " + System.currentTimeMillis());
                    if (c.compareTo(Calendar.getInstance()) < 0) {
                        Log.e(getClass().getName(), "Missed alarm at " + note.getReminder() + " for note id " + id);
                        String info = note.getReminder();
                        note.setReminder(util.Reminder_null);
                        repoistary.update(note);
                        resultIntent.setAction("REMINDER_NOTE_" + id);
                        NotificationCompat.Builder notif =
                                new NotificationCompat.Builder(context)
                                        .setSmallIcon(R.drawable.ic_stat_event_note)
                                        .setContentTitle("missed_reminder")
                                        .setContentText(content)
                                        .setSubText(info)
                                        .setShowWhen(false)
                                        .setColor(Color.argb(255, 32, 128, 200));

                        notif.setStyle(new NotificationCompat.BigTextStyle().bigText(content)
                                .setSummaryText(info));

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                        stackBuilder.addParentStack(addNotetest.class);
                        // Adds the Intent to the top of the stack
                        stackBuilder.addNextIntent(resultIntent);
                        // Gets a PendingIntent containing the entire back stack
                        PendingIntent resultPendingIntent =
                                stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

                        notif.setAutoCancel(true);
                        notif.setContentIntent(resultPendingIntent);
                        notif.setOngoing(false);
                        notif.setChannelId("reminders_channel");

                        // Builds the notification and issues it.
                        mNotifyMgr.notify(id, notif.build());

                    } else {
                        Log.d(getClass().getName(), "Settings alarm at " + note.getReminder() + " for note id " + id);
                        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmIntent);
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmIntent);
                        } else {
                            alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmIntent);
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

               if (note.isNotifcation()) {
                String info;

                NotificationCompat.Builder notif =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(R.drawable.ic_stat_event_note)
                                .setContentText(content)
                                 .setShowWhen(false)
                                 .setColor(Color.argb(255, 32, 128, 200));

                if (!TextUtils.isEmpty(title)) {
                    notif.setContentTitle(title);
                } else {
                    notif.setContentTitle(context.getString(R.string.Note));
                }

                notif.setStyle(new NotificationCompat.BigTextStyle().bigText(content));

                Class c;
                if (note.isIschecklist() )
                    c = addNotetest.class;
                else
                    c = add_checklistActicity.class;
                Intent resultIntent = new Intent(context, c);
                   bundle.putSerializable("notte", note);
                   resultIntent.putExtra(util.bakr_Note_Bundle, bundle);
                resultIntent.setAction("ACTION_NOTE_" + id);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addParentStack(addNotetest.class);
                // Adds the Intent to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                // Gets a PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);

                notif.setContentIntent(resultPendingIntent);
                notif.setOngoing(true);
                notif.setChannelId(util.CHANNEL_ID_NOTE);

                // Builds the notification and issues it.
                mNotifyMgr.notify(id, notif.build());
            }
        }






            return null;
        }
    }
}