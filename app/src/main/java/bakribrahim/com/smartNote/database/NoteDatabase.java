package bakribrahim.com.smartNote.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.models.checklistmodel;
import bakribrahim.com.smartNote.util.util;

@Database(entities = {Note.class, checklistmodel.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

      private  static NoteDatabase noteDatabase ;
      public abstract  Note_DAO note_dao( );
     Context con;

      public  static  synchronized NoteDatabase getinstance (Context con  )
      {
          if (noteDatabase ==null)
          {
              noteDatabase = Room.databaseBuilder(con.getApplicationContext(),
                      NoteDatabase.class ,"noteDatabase")
                      .fallbackToDestructiveMigration()
                      .addCallback(roomcallback).build();

          }
          return noteDatabase;

      }

      private  static   RoomDatabase.Callback roomcallback =new Callback() {
          @Override
          public void onCreate(@NonNull SupportSQLiteDatabase db) {

              super.onCreate(db);
              new papulatedatabase(noteDatabase.note_dao()).execute();
          }
          class papulatedatabase extends AsyncTask<Void, Void ,Void> {
              private  Note_DAO Dao;

              public papulatedatabase(Note_DAO dao) {
                  Dao = dao;
              }

              @Override
              protected Void doInBackground(Void... voids) {
                  Dao.insert_note(new Note("title00  ", "important", 1,false ,false,false,false,false, util.Reminder_null,util.DefalutColor ));
                  Dao.insert_note(new Note("title01  ", "important", 1,false ,false,false,false,false, util.Reminder_null,util.DefalutColor));
                  Dao.insert_note(new Note("title03 ", "important", 3,false ,false,false,false,false, util.Reminder_null,util.DefalutColor));
                  Dao.insert_note(new Note("title04  ", "important", 1,false ,false,false,false,false, util.Reminder_null,util.DefalutColor));

                  return null;
              }
          }

      };
}
