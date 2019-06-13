package bakribrahim.com.smartNote.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import bakribrahim.com.smartNote.R;

public class settingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean them = preferences.getBoolean("ThemPreferance", false);
        if (them)
        {
            setTheme(R.style.AppTheme_NoActionBar);

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contaier, new settingfragment())
                .commit();
    }


}
