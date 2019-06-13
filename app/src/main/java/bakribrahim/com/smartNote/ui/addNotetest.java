package bakribrahim.com.smartNote.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.onegravity.colorpicker.ColorPickerDialog;
import com.onegravity.colorpicker.ColorPickerListener;
import com.onegravity.colorpicker.SetColorPickerListenerEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.lifecycle.ViewModelProviders;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.Reciever.AlarmReceiver;
import bakribrahim.com.smartNote.util.util;
import bakribrahim.com.smartNote.viewmodel.NoteviewModel;
import bakribrahim.com.smartNote.viewmodel.newsViewModelFactory;

public class addNotetest extends AppCompatActivity {
    public static final String Extra_title ="bakribrahim.com.architecturecomponent.Extra_title";
    public static final String Extra_description ="bakribrahim.com.architecturecomponent.Extra_description";
    public static final String Extra_proirity ="bakribrahim.com.architecturecomponent.Extra_proirity";
    public static final String Extra_ID ="bakribrahim.com.architecturecomponent.Extra_proirity";
    public static final String Extra_archived ="bakribrahim.com.architecturecomponent.Extra_archived";
    public static final String Extra_favouit ="bakribrahim.com.architecturecomponent.Extra_favouit";
    NoteviewModel noteviewModel ;
    FloatingActionButton fab_note;
    View   bottom_bar ,toolbar;
    ImageView imDelete;
    boolean archived ,favouirt  ,edtie_mode=true;
     EditText title ,desc;
     int ID;
    private int cx, cy;
    float radius;
    Note note ;
    String reminder;
    int noOfTimesCalledDate ,noOfTimesCalledTime  ;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), readableDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
    TextView txt_reminder;
    boolean add_operation;
    Boolean darkThem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
          darkThem = preferences.getBoolean("ThemPreferance", false);
        if (darkThem)
        {
            setTheme(R.style.AppTheme_NoActionBar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        title= findViewById(R.id.edt_titile);
        desc= findViewById(R.id.edt_desc);
        bottom_bar = findViewById(R.id.bottom_bar);
        toolbar=findViewById(R.id.toolbar);
        fab_note=findViewById(R.id.fab_note);
         noteviewModel = ViewModelProviders.of(this,new newsViewModelFactory(this.getApplication(), util.Allnotes)).get(NoteviewModel.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.addnotetoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent data =getIntent();
        note= (Note) data.getSerializableExtra("note");
        if (note !=null){
            getSupportActionBar().setTitle(" Edite  Note ");
            if (note.isTrash())
            {
                Toast.makeText(this, "isTrash", Toast.LENGTH_SHORT).show();
                showDialogeWhenTrash();

            }
            add_operation=false;
            title.setText(note.getTitle());
            desc.setText(note.getDescription());
            ID=note.getID();
            archived =note.isArchive();
            favouirt =note.isFavourite();
            if (note.getBackgroundColor()!=util.DefalutColor)
                ((CardView)findViewById(R.id.note_card)).setBackgroundColor(note.getBackgroundColor());
        }
        else {
            getSupportActionBar().setTitle("Add New Note ");
            note=new Note();
            note.setBackgroundColor(util.DefalutColor);
            note.setReminder(util.Reminder_null);
            add_operation=true;
        }

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Save Note ? ")
                .setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (note.getTitle()==null)
                        {
                            exit();

                        }
                        else
                        {
                            save_data();
                            exit();

                        }

                     }
                })
                .setNeutralButton(R.string.str_cancle, null)
                .setNegativeButton(R.string.str_No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exit();

                    }
                })
                .show();

    }
    private void exit() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        Toast.makeText(this, "menu", Toast.LENGTH_SHORT).show();
         return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                save_data();
                finish();
                break;
        }
            return  true;
    }



       void save_data(){
         if (note.getID()==0)
            noteviewModel.insert(note, null);
            else
            noteviewModel.update(note);

       }

    public void onClick(View view) {
        String stitle =title.getText().toString();
       String sdesc =desc.getText().toString();
        if (stitle.isEmpty()|| sdesc.isEmpty())
        {
            Toast.makeText(this, "please insert data in title or content", Toast.LENGTH_SHORT).show();

        }
       else {
            note.setTitle(stitle);
            note.setDescription(sdesc);
            if (edtie_mode)
            {
                // show tool bar  and enable to note
                Toast.makeText(this, "edtie_mode"+edtie_mode, Toast.LENGTH_SHORT).show();
                edtie_mode=false;
                revealToolbar();
                bottom_bar.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                fab_note.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                title.setEnabled(false);
                desc.setEnabled(false);

            }
            else
            {

                edtie_mode=true;
                hideToolbar();
                toolbar.setVisibility(View.INVISIBLE);
                fab_note.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                title.setEnabled(true);
                desc.setEnabled(true);
            }
        }



    }



    private void revealToolbar() {
        if (Build.VERSION.SDK_INT >= 21) {
            cx = bottom_bar.getWidth() / 2;
            cy = bottom_bar.getHeight() / 2;
            radius = (float) Math.hypot(cx, cy);

            // create the animator for this view (the start radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(bottom_bar, cx, cy, 0, radius);

            // make the view visible and start the animation
            bottom_bar.setVisibility(View.VISIBLE);
            anim.start();
        }
        else bottom_bar.setVisibility(View.VISIBLE);
    }

    private void hideToolbar() {
        if (Build.VERSION.SDK_INT >= 21) {
            cx = bottom_bar.getWidth() / 2;
            cy = bottom_bar.getHeight() / 2;
            radius = (float) Math.hypot(cx, cy);

            // create the animation (the final radius is zero)
            Animator anim = ViewAnimationUtils.createCircularReveal(bottom_bar, cx, cy, radius, 0);

            // make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    bottom_bar.setVisibility(View.INVISIBLE);
                }
            });

            // start the animation
            anim.start();
        } else bottom_bar.setVisibility(View.INVISIBLE);
    }
    public void share(View v) {
        StringBuffer shareText = new StringBuffer();
        if (!title.getText().toString().trim().equals("")) {
            shareText.append(title.getText().toString().trim()).append("\n");
        }

        shareText.append(desc.getText().toString());
         Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        share.setType("text/plain");
        if (share.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(share,  "share_via"));

        } else {
            Toast.makeText(this, "no_share_app_found", Toast.LENGTH_SHORT).show();
        }
    }


    public void notifBtn(View v) {
        BottomSheetDialog bottomSheetDialog;
        if (darkThem)
            bottomSheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheet_Dark);
        else
            bottomSheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheet_light);
        bottomSheetDialog.setContentView(R.layout.notifaction_bottom_sheet);
        txt_reminder = bottomSheetDialog.findViewById(R.id.reminder_time);
        Switch notifSwitch = bottomSheetDialog.findViewById(R.id.notification_switch);
        final Switch reminderSwitch = bottomSheetDialog.findViewById(R.id.reminder_switch);

        if (note.isNotifcation())
      {
          notifSwitch.setChecked(true);
      }
      if (!note.getReminder().equals(util.Reminder_null))
      {
          reminderSwitch.setChecked(true);
          txt_reminder.setText(note.getReminder());
          txt_reminder.setVisibility(View.VISIBLE);
      }
        notifSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)
                {
                    note.setNotifcation(true);
                     noteviewModel.update(note);
                    notif(1);
                }
                else
                {
                    note.setNotifcation(false);
                    noteviewModel.update(note);
                    notif(0);
                }

            }
        });
        reminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                      @Override
                                                      public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                                                          //setupAlarm();
                                                          if(checked){
                                                              // setup alarm
                                                              //update note in database
                                                              setupAlarm();

                                                          }
                                                          else
                                                          {
                                                           note.setReminder(util.Reminder_null);
                                                           //noteviewModel.update(note);
                                                           txt_reminder.setVisibility(View.GONE);
                                                              toggleReminder(false);
                                                              Toast.makeText(addNotetest.this, "alarm cancled", Toast.LENGTH_SHORT).show();
                                                          }
                                                      }
                                                  }
         );
        bottomSheetDialog.show();
    }
     public void notif(int notified) {
         NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notified == 0) {
            mNotifyMgr.cancel(note.getID());
        } else {
            String info;

            NotificationCompat.Builder notif =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_event_note)
                            .setContentText(note.getDescription())
                            .setSubText(note.getTitle())
                            .setShowWhen(false)
                            .setCategory("Note")
                            .setColor(Color.argb(255, 32, 128, 200));

                 notif.setContentTitle(note.getTitle());


            notif.setStyle(new NotificationCompat.BigTextStyle().bigText(note.getDescription()));
            // Sets an ID for the notification
             Intent resultIntent = new Intent(this, addNotetest.class);
            Bundle bundle = new Bundle();
            resultIntent.putExtra("note", note);
             resultIntent.setAction("ACTION_NOTE_" + note.getID());

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(addNotetest.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            // Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(note.getID()
                            , PendingIntent.FLAG_UPDATE_CURRENT);

            notif.setContentIntent(resultPendingIntent);
            notif.setOngoing(true);

            // Builds the notification and issues it.
            mNotifyMgr.notify(note.getID(), notif.build());

        }


    }

    public void setupAlarm(){
        noOfTimesCalledDate = 0;
        noOfTimesCalledTime = 0;
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                addNotetest.this,R.style.TimePickerTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int year, final int month, final int day) {
                        if (noOfTimesCalledDate % 2 == 0) {
                            new TimePickerDialog(addNotetest.this,R.style.TimePickerTheme,
                                    new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                            if (noOfTimesCalledTime % 2 == 0) {
                                                calendar.set(year, month, day, hour, min);
                                                calendar.set(Calendar.SECOND, 0);
                                                if (calendar.compareTo(Calendar.getInstance()) <= 0) {
                                                    Toast.makeText(addNotetest.this, "past time", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                reminder = readableDateFormat.format(calendar.getTime());
                                                ID = note.getID();
                                                note.setReminder(reminder);
                                               //noteviewModel.update(note);
                                                txt_reminder.setText(reminder);
                                                txt_reminder.setVisibility(View.VISIBLE);
                                                toggleReminder(true);
                                                noOfTimesCalledTime++;
                                            }
                                        }
                                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(
                                    addNotetest.this)).show();
                            noOfTimesCalledDate++;
                        }
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();




    }
    public void archive(View v) {
        BottomSheetDialog bottomSheetDialog;
        if (darkThem)
            bottomSheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheet_Dark);
        else
            bottomSheetDialog = new BottomSheetDialog(this,
                    R.style.BottomSheet_light);
        bottomSheetDialog.setContentView(R.layout.archive_bottom_sheet);
         Switch addarchiveSwitch = bottomSheetDialog.findViewById(R.id.addarchiveSwitch);
        final Switch addfavouirtSwitch = bottomSheetDialog.findViewById(R.id.addfavouirtSwitch);
        if (archived)
        {
            addarchiveSwitch.setChecked(true);
            addfavouirtSwitch.setEnabled(false);
        }
        if (favouirt)
            addfavouirtSwitch.setChecked(true);

        addarchiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
              if (isChecked)
              {
                  note.setArchive(true);
                  Toast.makeText(addNotetest.this, "add to archive", Toast.LENGTH_SHORT).show();
                  if (addfavouirtSwitch.isChecked())
                      addfavouirtSwitch.setChecked(false);
                  addfavouirtSwitch.setEnabled(false);

                  note.setFavourite(false);
              }
              else
              {
                  note.setArchive(false);
                  Toast.makeText(addNotetest.this, "unarchive", Toast.LENGTH_SHORT).show();
                  addfavouirtSwitch.setEnabled(true);
              }
            }
        });

        addfavouirtSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    note.setFavourite(true);
                    note.setArchive(false);
                    Toast.makeText(addNotetest.this, "added to favouirt", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    note.setFavourite(false);
                    Toast.makeText(addNotetest.this, "Removed from favouirt", Toast.LENGTH_SHORT).show();
                }

            }
        });

        bottomSheetDialog.show();
    }

    public void delete(View view) {
         if (note.isTrash())
         {
             new AlertDialog.Builder(this)
                     .setMessage("can't undo this operation")
                     .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             //noteviewModel.delete(note);
                             finish();
                         }
                     })
                     .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                         }
                     })
                     .show();
         }
         else
         {
             note.setTrash(true);
             note.setArchive(false);
             note.setFavourite(false);
             //noteviewModel.update(note);
             Toast.makeText(addNotetest.this, "Moved to Trash", Toast.LENGTH_SHORT).show();
         }


    }
    private void toggleReminder(boolean enable) {
        Log.e("bakrbekooo", "start toggleReminder");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt(util.bakr_note_ID, note.getID());
        bundle.putSerializable("notte", note);

        intent.putExtra(util.bakr_Note_Bundle, bundle);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,
                note.getID(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (enable) {

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(readableDateFormat.parse(reminder));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            alarmManager.cancel(alarmIntent);

        }
    }

    int     mSelectedColor;
    public void pickColor(View view) {
        ColorPickerListener listener=new ColorPickerListener() {
            @Override
            public void onDialogClosing() {
          note.setBackgroundColor(mSelectedColor);
             }

            @Override
            public void onColorChanged(int color) {
            mSelectedColor=color;
            ((CardView)findViewById(R.id.note_card)).setBackgroundColor(mSelectedColor);
        }
    };
      int  mDialogId = new ColorPickerDialog(this,R.attr.backgroundcolor, true).show();
        SetColorPickerListenerEvent.setListener(mDialogId, listener);
    }


    // this method used when before edit note that come from trash
    public void showDialogeWhenTrash()
    {
        new AlertDialog.Builder(this)
                .setMessage("Can't Edite This Note Before Untrash , Untrash Now ?")
                .setCancelable(false)
                .setPositiveButton("Untrash", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        note.setTrash(false);
                        noteviewModel.update(note);
                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }
}
