package bakribrahim.com.smartNote.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import bakribrahim.com.smartNote.BuildConfig;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.util.util;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton fab;
    View addNoteView, addListView, shadow;
    View.OnClickListener fabClickListener, addNoteListener, addListListener;
    boolean  fabOpen =false;
    boolean themchanged ;
    boolean themchangedtest ;
    Toolbar toolbar;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          preferences = PreferenceManager.getDefaultSharedPreferences(this);
          themchanged = preferences.getBoolean("ThemPreferance", false);
        if (themchanged)
        {
            setTheme(R.style.AppTheme_NoActionBar);
            themchangedtest=themchanged;
        }
        setContentView(R.layout.activity_main2);
          toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
         navigationView.setNavigationItemSelectedListener(this);

        //___________________________
         fab = findViewById(R.id.fab);
         addNoteView = findViewById(R.id.note_fab);
         addListView = findViewById(R.id.list_fab);
         shadow = findViewById(R.id.shadow);

         fabClickListener = new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if (fabOpen) {
                     closeFabMenu();
                 } else {
                     showFabMenu();
                 }
             }
         };
         addNoteListener = new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 closeFabMenu();
                 Intent i = new Intent(Main2Activity.this,addNotetest.class);
                 startActivity(i);
              }
         };
         addListListener = new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 closeFabMenu();
                 Intent i = new Intent(Main2Activity.this,add_checklistActicity.class);
                 startActivity(i);
             }
         };

        fab.setOnClickListener(fabClickListener);
         shadow.setOnClickListener(fabClickListener);


         //show all notes as defualt
        // show setting fragment when recreate actiiviy when change darkThem

            showNoteFragment(util.Allnotes);

     }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    private void showFabMenu() {
        fab.setOnClickListener(null);
        fab.animate().rotationBy(45f).setDuration(300).start();
        fabOpen = true;
        addNoteView.setVisibility(View.VISIBLE);
        addNoteView.setAlpha(0f);
        addNoteView.animate().translationYBy(convertDpToPixel(-52)).alpha(1f).setDuration(300).start();
        addListView.setVisibility(View.VISIBLE);
        addListView.setAlpha(0f);
        addListView.animate().translationYBy(convertDpToPixel(-100)).alpha(1f).setDuration(300).start();
        shadow.setVisibility(View.VISIBLE);
        shadow.setAlpha(0f);
        shadow.animate().alpha(1f).setDuration(300).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fab.setOnClickListener(fabClickListener);
                addNoteView.setOnClickListener(addNoteListener);
                addListView.setOnClickListener(addListListener);
            }
        }, 300);
    }

    private void closeFabMenu() {
        fab.setOnClickListener(null);
        ObjectAnimator.ofFloat(fab, "rotation", 45f, 0f).start();
        fabOpen = false;
        addNoteView.animate().translationYBy(convertDpToPixel(52)).alpha(0f).setDuration(300).start();
        addListView.animate().translationYBy(convertDpToPixel(100)).alpha(0f).setDuration(300).start();
        shadow.animate().alpha(0f).setDuration(300).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addNoteView.setVisibility(View.GONE);
                addListView.setVisibility(View.GONE);
                shadow.setVisibility(View.GONE);
                addNoteView.setOnClickListener(null);
                addListView.setOnClickListener(null);
                fab.setOnClickListener(fabClickListener);
            }
        }, 300);
    }

    public float convertDpToPixel(float dp) {
        return dp * getResources().getDisplayMetrics().density;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String fragmenttype="";
        if (id == R.id.addchecklist) {
            toolbar.setTitle("Checklist Notes");
            showNoteFragment(util.CHECKlist);
        }

        if (id == R.id.nav_Allnotes) {
            toolbar.setTitle("My Notes");
            showNoteFragment(util.Allnotes);
        }

        else if (id == R.id.nav_favourte) {
            toolbar.setTitle("Favouite Notes");

            showNoteFragment(util.favouriteeNotes);


        } else if (id == R.id.nav_Reminder) {
            toolbar.setTitle("Remider Notes");

            showNoteFragment(util.ReminderNotes);

        }  else if (id == R.id.nav_archive) {
            toolbar.setTitle("Archive Notes");

            showNoteFragment(util.ArchiveNotes);


        } else if (id == R.id.nav_trash) {
            toolbar.setTitle("Trash Notes");

            showNoteFragment(util.trashNotes);
        }
        else if (id == R.id.nav_share) {
            sharapplication();
        }
      else if (id == R.id.nav_setting) {
            startActivityForResult(new Intent(Main2Activity.this,settingActivity.class),100);
           }
        else if (id == R.id.nav_Developer) {
            String linkedinProfile="https://www.linkedin.com/in/bakr-brahim";
            Uri uri = Uri.parse(linkedinProfile);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
         }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

       public void showNoteFragment(String fragmentType){
           FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
           showNotesFrag fragment1 =new showNotesFrag();
           Bundle bundle = new Bundle();
           bundle.putString("fragmentType", fragmentType);
           fragment1.setArguments(bundle);
           fragmentTransaction.replace(R.id.container, fragment1);
           fragmentTransaction.commit();
       }

//    public void showSettingFragment(){
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, new settingfragment())
//                .commit();
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void sharapplication()
    {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }
}
