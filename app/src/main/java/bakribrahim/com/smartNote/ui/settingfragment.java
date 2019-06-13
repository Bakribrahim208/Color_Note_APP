package bakribrahim.com.smartNote.ui;

import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;


import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.Reciever.BootReceiver;
import bakribrahim.com.smartNote.util.util;
import bakribrahim.com.smartNote.viewmodel.NoteviewModel;
import bakribrahim.com.smartNote.viewmodel.newsViewModelFactory;

import static android.content.Context.NOTIFICATION_SERVICE;

public class settingfragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener  , Preference.OnPreferenceChangeListener {
    SharedPreferences preferences;
    Preference clearnotif ,clearReminder ,Deleteallnote ,ringtone;
    NoteviewModel noteviewModel ;
    SwitchPreference ThemPreferance ,AlarmSound ,alarmvibrate,alarmLed;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
          preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean them = preferences.getBoolean("ThemPreferance", false);
        if (them)
        {
             getActivity().setTheme(R.style.AppTheme_NoActionBar);


         }

        setPreferencesFromResource(R.xml.setting, rootKey);
        noteviewModel = ViewModelProviders.of(this,new newsViewModelFactory(getActivity().getApplication(), util.Allnotes)).get(NoteviewModel.class);
        clearnotif = findPreference("clearnotif");
        clearnotif.setOnPreferenceClickListener(settingfragment.this);
        clearReminder = findPreference("clearReminder");
        Deleteallnote = findPreference("Deleteallnote");
         Deleteallnote.setOnPreferenceClickListener(settingfragment.this);
        clearReminder.setOnPreferenceClickListener(settingfragment.this);

        ThemPreferance = (SwitchPreference) getPreferenceManager().findPreference("ThemPreferance");
        AlarmSound= (SwitchPreference) getPreferenceManager().findPreference("AlarmSound");
        AlarmSound.setEnabled(true);
        alarmvibrate= (SwitchPreference) getPreferenceManager().findPreference("alarmvibrate");
        alarmLed= (SwitchPreference) getPreferenceManager().findPreference("alarmLed");


        alarmvibrate.setOnPreferenceChangeListener(this);
        AlarmSound.setOnPreferenceChangeListener(this);
        ThemPreferance.setOnPreferenceChangeListener( this);
        alarmLed.setOnPreferenceChangeListener( this);

    }
    public String getDataFromPreferance(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getContext() /* Activity context */);
        String country = sharedPreferences.getString("list_preference_1", "eg");

        return  country;
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey()==clearnotif.getKey())
        {
            clearAllNotifications();
        }
        else if (preference.getKey()==clearReminder.getKey())
        {
            clearAllReminders();
        }
        else if (preference.getKey()==Deleteallnote.getKey())
        {
            Deleteallnote_()  ;
        }
        else if (preference.getKey()==ringtone.getKey())
        {
            onPreferenceRingToneClick( )   ;
        }
        return false;
    }

    public void clearAllNotifications() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(R.string.confirm_clear_notifications_message)
                .setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       noteviewModel.Update_AllNOtificationNotes();
                        NotificationManager mNotifyMgr = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                        mNotifyMgr.cancelAll();
                        new BootReceiver().onReceive(getContext(), null);
                     }
                })
                .setNegativeButton(R.string.str_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void clearAllReminders() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext() );
        dialog.setMessage(R.string.confirm_clear_Reminder_message)
                .setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         noteviewModel.Update_AllRemindersNotes();
                       // new BootReceiver().onReceive(SettingsActivity.this, null);
                     }
                })
                .setNegativeButton(R.string.str_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void Deleteallnote_() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(R.string.confirm_clearAllNote_message)
                .setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        noteviewModel.deleteAll();
                     }
                })
                .setNegativeButton(R.string.str_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void settingAlaramsound(){

    }


    int REQUEST_CODE_ALERT_RINGTONE =100;
    public boolean onPreferenceRingToneClick( ) {
             Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, Settings.System.DEFAULT_NOTIFICATION_URI);

            String existingValue =  preferences.getString(ringtone.getKey(), ""); // TODO
            if (existingValue != null) {
                if (existingValue.length() == 0) {
                    // Select "Silent"
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);
                } else {
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(existingValue));
                }
            } else {
                // No ringtone has been selected, set to the default
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Settings.System.DEFAULT_NOTIFICATION_URI);
            }

            startActivityForResult(intent, REQUEST_CODE_ALERT_RINGTONE);
            return true;
        }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ALERT_RINGTONE && data != null) {
            Uri ringtoner = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (ringtone != null) {

                preferences.edit().putString(ringtone.getKey(),ringtoner.toString()); // TODO
                Log.e("ringtone", ringtoner.toString());
            } else {
                // "Silent" was selected
                preferences.edit().putString(ringtone.getKey(),""); // TODO
                Log.e("ringtone", "Silent" );

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean boolVal = (Boolean)newValue;

        if (preference.getKey()==AlarmSound.getKey())
        {
             preferences.edit().putBoolean("AlarmSound",boolVal ).commit();
             AlarmSound.setChecked(boolVal );         }
          else if (preference.getKey()==ThemPreferance.getKey())
        {
              preferences.edit().putBoolean("ThemPreferance",boolVal ).commit();
             startActivity(new Intent(getContext(),settingActivity.class));
              getActivity().finish();
            //getActivity().recreate();
        }
        else if (preference.getKey() ==alarmvibrate.getKey())
        {
             preferences.edit().putBoolean(alarmvibrate.getKey(),boolVal ).commit();

              alarmvibrate.setChecked(boolVal );
        }

        else if (preference.getKey() ==alarmLed.getKey())
        {
             preferences.edit().putBoolean(alarmLed.getKey(),boolVal ).commit();
              alarmLed.setChecked(boolVal );

        }
        return false;
    }


}