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
import android.text.TextUtils;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.Reciever.AlarmReceiver;
import bakribrahim.com.smartNote.models.checklistmodel;
import bakribrahim.com.smartNote.adapters.editechecklist_adpater;
import bakribrahim.com.smartNote.util.util;
import bakribrahim.com.smartNote.viewmodel.NoteviewModel;
import bakribrahim.com.smartNote.viewmodel.newsViewModelFactory;

public class add_checklistActicity extends AppCompatActivity implements editechecklist_adpater.itemCHeck  {
     TextView txt_reminder;
    View customview ;
    EditText content_view
            ,edt_titile;
    FloatingActionButton fab_note;
    View   bottom_bar ,toolbar;
    ImageView add_view  ;
    RecyclerView recycleview;
    editechecklist_adpater checklistAdapter;
     private int  cx,
                  cy ,
                  ID,
                  mSelectedColor
                 ,noOfTimesCalledDate
                  ,noOfTimesCalledTime ;
     float radius;
    ArrayList<checklistmodel>checklistmodels= new ArrayList<>();
    ArrayList<checklistmodel>Deletedchecklist= new ArrayList<>();
    NoteviewModel noteviewmodel ;
    Note note;
    Boolean darkThem;
    Boolean addmode =false   // add or edite mode
    ,       archived
    ,      favouirt
    ,     edtie_mode=true;
      String reminder;
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                      readableDateFormat = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
          darkThem = preferences.getBoolean("ThemPreferance", false);
        if (darkThem)
        {
            setTheme(R.style.AppTheme_NoActionBar);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addchechlist);
        Toolbar mytoolbar = (Toolbar) findViewById(R.id.addnotetoolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        edt_titile =findViewById(R.id.edt_titile);
        customview =findViewById(R.id.add_checklistView);
        add_view=customview.findViewById(R.id.add_view);
        content_view=customview.findViewById(R.id.viewcontent);
        bottom_bar = findViewById(R.id.bottom_bar);
        toolbar=findViewById(R.id.toolbar);
        fab_note=findViewById(R.id.fab_note);
        recycleview =findViewById(R.id.recycleview);
        RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(this);
        recycleview.setLayoutManager(layoutManager);
        checklistAdapter =new editechecklist_adpater(this);
        recycleview.setAdapter(checklistAdapter);
        checklistAdapter.setData(checklistmodels);
        noteviewmodel = ViewModelProviders.of(this,new newsViewModelFactory(this.getApplication(),util.CHECKlist)).get(NoteviewModel.class);
        Intent data =getIntent();
        note= (Note) data.getSerializableExtra("note");
        if (note !=null){

            if (note.isTrash())
            {
                Toast.makeText(this, "isTrash", Toast.LENGTH_SHORT).show();
                showDialogeWhenTrash();

            }

            addmode =false;
            getSupportActionBar().setTitle(" Edite  Note ");
            ID=note.getID();
            archived =note.isArchive();
            favouirt =note.isFavourite();
            if (note.getBackgroundColor()!=util.DefalutColor)
                ((CardView)findViewById(R.id.note_card)).setBackgroundColor(note.getBackgroundColor());            edt_titile.setText(note.getTitle());
             noteviewmodel.getchecklistwhereid(note.getID()).observe(this, new Observer<List<checklistmodel>>() {
                @Override
                public void onChanged(List<checklistmodel> checklistmodelList) {
                    checklistmodels.clear();
                    checklistmodels.addAll( checklistmodelList );
                    checklistAdapter.notifyDataSetChanged();
                }
            });

        }
        else {
            note =new Note();
            note.setBackgroundColor(util.DefalutColor);
            note.setReminder(util.Reminder_null);
            note.setIschecklist(true);
             addmode=true;
         }
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Save Note ? ")
                .setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (edt_titile.getText().toString().isEmpty())
                        {

                            Toast.makeText(add_checklistActicity.this, "note.getTitle()==null", Toast.LENGTH_SHORT).show();
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
         return  true;
    }
     @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

        }
        return  true;
    }


    public void add_item(View view) {
         String Notetxt=content_view.getText().toString();
         if (!TextUtils.isEmpty(Notetxt))
         {
             if (note.getID()==0 )
             {
                 checklistmodels.add( new checklistmodel(0, Notetxt , false,false));
             }
             else
             {
                 checklistmodels.add( new checklistmodel(note.getID(),Notetxt, false,true));

             }
         }
         else
             Toast.makeText(this, " please type note title ", Toast.LENGTH_SHORT).show();

        checklistAdapter.notifyDataSetChanged();
        content_view.setText("");

    }

    void save_data(){
        note.setTitle(edt_titile.getText().toString());
        note.setDescription(util.Note_CONTEN_Checklist);
        if (note.getID()==0)
        {
            Long l =noteviewmodel.insert(note,checklistAdapter.GetAllNotes());
            Toast.makeText(this, "insert"+checklistAdapter.GetAllNotes().size(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            // upadate the checklist that updated and add new check list
              ArrayList<checklistmodel>newchecklistitem=new ArrayList<>();
            for (int i=0 ; i<checklistmodels.size();i++)
            {
                if (checklistmodels.get(i).isDeleted())
                {
                    checklistmodels.get(i).setDeleted(false);
                    newchecklistitem.add( checklistmodels.get(i) );

                }
                else
                {
                    noteviewmodel.update_checklist(checklistmodels.get(i));
                 }

            }

            noteviewmodel.update(note);

            noteviewmodel.insert_checklist(newchecklistitem);
            Toast.makeText(this, "update", Toast.LENGTH_SHORT).show();
            // Delete chechlist from database that user had deleted before
            if (Deletedchecklist.size()>0)
                for (int i=0 ; i<Deletedchecklist.size();i++)
                noteviewmodel.Delete_checklist(Deletedchecklist.get(i));

        }
    }

    public void onClick(View view) {



        String titile  =edt_titile.getText().toString();
        StringBuffer content = new StringBuffer();

        if (!titile.isEmpty()){
            if (edtie_mode)
            {
                // show tool bar  and enable to note
                 edtie_mode=false;
                revealToolbar();
                bottom_bar.setVisibility(View.VISIBLE);
                toolbar.setVisibility(View.VISIBLE);
                fab_note.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                edt_titile.setEnabled(false);
                content_view.setEnabled(false);
                add_view.setEnabled(false);

            }
            else
            {

                edtie_mode=true;
                hideToolbar();
                toolbar.setVisibility(View.INVISIBLE);
                fab_note.setImageResource(R.drawable.ic_mode_edit_black_24dp);
                edt_titile.setEnabled(true);
                content_view.setEnabled(true);
                add_view.setEnabled(true);

            }
        }
        else {
            Toast.makeText(add_checklistActicity.this, "insert titile please", Toast.LENGTH_SHORT).show();
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
        shareText.append(edt_titile.getText().toString()+"\n");
        shareText.append("\n"+convertListtoString ());
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        share.setType("text/plain");
        if (share.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(share,  "Share Via"));

        } else {
            Toast.makeText(this, "No Share APP Found", Toast.LENGTH_SHORT).show();
        }
    }



    public String convertListtoString (){
        StringBuffer text = new StringBuffer();
//        Log.e("listnote","checked???"+"[\u2716]" );//✖
//        Log.e("listnote","checked???"+"[\u2714]" );//✔

        for (int i =0 ; i <checklistmodels.size() ; i++)
        {
            if (checklistmodels.get(i).isChecked())
            {
            text.append(checklistmodels.get(i).getContent()+"\t"+"[\u2714]"+"\n");
            }
            else
            {
                text.append(checklistmodels.get(i).getContent()+"\t"+"[\u2716]"+"\n");

            }
        }
        return text.toString();
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
                    noteviewmodel.update(note);
                    notif(1);
                }
                else
                {
                    note.setNotifcation(false);
                    noteviewmodel.update(note);
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
                                                              noteviewmodel.update(note);
                                                              txt_reminder.setVisibility(View.GONE);
                                                              toggleReminder(false);
                                                              Toast.makeText(add_checklistActicity.this, "alarm cancled", Toast.LENGTH_SHORT).show();
                                                          }
                                                      }
                                                  }
        );
        bottomSheetDialog.show();
    }
    public void setupAlarm(){
        noOfTimesCalledDate = 0;
        noOfTimesCalledTime = 0;
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(add_checklistActicity.this
                ,R.style.TimePickerTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int year, final int month, final int day) {
                        if (noOfTimesCalledDate % 2 == 0) {
                            new TimePickerDialog(add_checklistActicity.this,R.style.TimePickerTheme,
                                    new TimePickerDialog.OnTimeSetListener() {
                                        @Override
                                        public void onTimeSet(TimePicker timePicker, int hour, int min) {
                                            if (noOfTimesCalledTime % 2 == 0) {
                                                calendar.set(year, month, day, hour, min);
                                                calendar.set(Calendar.SECOND, 0);
                                                if (calendar.compareTo(Calendar.getInstance()) <= 0) {
                                                    Toast.makeText(add_checklistActicity.this, "past time", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                                reminder = readableDateFormat.format(calendar.getTime());
                                                ID = note.getID();
                                                note.setReminder(reminder);
                                                noteviewmodel.update(note);
                                                txt_reminder.setText(reminder);
                                                txt_reminder.setVisibility(View.VISIBLE);
                                                toggleReminder(true);
                                                noOfTimesCalledTime++;
                                            }
                                        }
                                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(
                                    add_checklistActicity.this)).show();
                            noOfTimesCalledDate++;
                        }
                    }
                },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();




    }
    private void toggleReminder(boolean enable) {
        Log.e("bakrbekooo", "start toggleReminder");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt(util.bakr_note_ID, note.getID());
        Note tempnote =note;
        note.setDescription(convertListtoString());
        bundle.putSerializable("notte", tempnote);

        intent.putExtra(util.bakr_Note_Bundle, bundle);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,
                note.getID(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (enable) {
            Log.e("bakrbekooo", "enable");

            Calendar calendar = Calendar.getInstance();
            try {
                calendar.setTime(readableDateFormat.parse(reminder));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.e("bakrbekooo", "M");
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    Log.e("bakrbekooo", "KITKAT");

                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                    Log.e("bakrbekooo", "RTC_WAKEUP");

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            alarmManager.cancel(alarmIntent);
            Log.e("bakrbekooo", "cancel");

        }
    }
    public void notif(int notified) {
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notified == 0) {
            mNotifyMgr.cancel(note.getID());
        } else {
            String info =convertListtoString();
          Log.e("info", info);
            NotificationCompat.Builder notif =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_event_note)
                            .setContentText(info)
                             .setShowWhen(false)
                            .setCategory("note")
                            .setColor(Color.argb(255, 32, 128, 200));

            notif.setContentTitle(note.getTitle());


            notif.setStyle(new NotificationCompat.BigTextStyle().bigText(info));
            // Sets an ID for the notification
            Intent resultIntent = new Intent(this, add_checklistActicity.class);
            Bundle bundle = new Bundle();
            resultIntent.putExtra("note", note);
            resultIntent.setAction("ACTION_NOTE_" + note.getID());

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(addNote.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            // Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(note.getID()
                            , PendingIntent.FLAG_UPDATE_CURRENT);

            notif.setContentIntent(resultPendingIntent);
            notif.setOngoing(true);
            notif.setChannelId("notes_channel");

            // Builds the notification and issues it.
            mNotifyMgr.notify(note.getID(), notif.build());

        }


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
                    Toast.makeText(add_checklistActicity.this, "add to archive", Toast.LENGTH_SHORT).show();
                    if (addfavouirtSwitch.isChecked())
                        addfavouirtSwitch.setChecked(false);
                    addfavouirtSwitch.setEnabled(false);

                    note.setFavourite(false);

                }
                else
                {
                     note.setArchive(false);
                      Toast.makeText(add_checklistActicity.this, "unarchive", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(add_checklistActicity.this, "added to favouirt", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    note.setFavourite(false);
                    Toast.makeText(add_checklistActicity.this, "Removed from favouirt", Toast.LENGTH_SHORT).show();
                }
                noteviewmodel.update(note);

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
                            noteviewmodel.delete(note);
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
            noteviewmodel.getchecklistwhereid(0).observe(this, new Observer<List<checklistmodel>>() {
                @Override
                public void onChanged(List<checklistmodel> checklistmodelList) {
                    for (int i=0 ; i<checklistmodelList.size();i++)
                        noteviewmodel.Delete_checklist(checklistmodelList.get(i));                                }
            });
            Toast.makeText(this, "deleted isIschecklist successfully", Toast.LENGTH_SHORT).show();
            noteviewmodel.update(note);
            Toast.makeText(add_checklistActicity.this, "Moved to Trash", Toast.LENGTH_SHORT).show();
        }


    }
    public void pickColor(View view) {
        ColorPickerListener listener=new ColorPickerListener() {
            @Override
            public void onDialogClosing() {
                note.setBackgroundColor(mSelectedColor);
                noteviewmodel.update(note);
             }

            @Override
            public void onColorChanged(int color) {
                mSelectedColor=color;
                ((CardView)findViewById(R.id.note_card)).setBackgroundColor(mSelectedColor);
            }
        };
        int    mDialogId = new ColorPickerDialog(this, R.color.colorAccent, true).show();
        SetColorPickerListenerEvent.setListener(mDialogId, listener);
    }

    @Override
    public void itemCHeck(checklistmodel checklistmodel) {

    }

    @Override
    public void Deletechlistmodel(checklistmodel checklistmodel) {
        Deletedchecklist.add(checklistmodel);
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
                        noteviewmodel.update(note);
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
