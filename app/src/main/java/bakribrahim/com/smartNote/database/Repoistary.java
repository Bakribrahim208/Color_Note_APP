package bakribrahim.com.smartNote.database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.models.checklistmodel;
import bakribrahim.com.smartNote.util.util;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class Repoistary {
    private Note_DAO note_dao;
    LiveData<List<Note>>allnotes;
    LiveData<List<checklistmodel>>allchecklistmodel;
     public Repoistary(Context application , String type ) {
         note_dao=NoteDatabase.getinstance(application).note_dao();
        if (type== util.CHECKlist)
        {
            allnotes =note_dao.GetchecklistNotes();


        }
        if (type== util.Allnotes)
        {
            allnotes =note_dao.GetAllNote();


        }
        else if(type==util.ArchiveNotes)
        {
            allnotes = note_dao.GetArchiveNotes();
         }

        else if (type==util.trashNotes)
        {
            allnotes = note_dao.GetTrashNotes();

        }
        else if (type==util.favouriteeNotes)
        {
            allnotes = note_dao.GetFavoriteeNotes();

        }

        else if (type==util.ReminderNotes)
        {
            allnotes = note_dao.GetReminders(util.Reminder_null);

        }
      }
    long value=0;

     public long insert(final Note note,List<checklistmodel> checklistmodelList) {
       Completable.fromAction(new Action() {
           @Override
           public void run() throws Exception {
             value =  note_dao.insert_note(note);
            }
       }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
       .subscribe(new CompletableObserver() {
           @Override
           public void onSubscribe(Disposable d) {

               Log.e("bakrLOginng","onSubscribe");
           }

           @Override
           public void onComplete() {
               if (checklistmodelList!=null)
               {
                   for (int i =0 ;i<checklistmodelList.size();i++)
                   {
                       checklistmodelList.get(i).setNoteID(((int) value));

                   }

               }
               insert_checklist(checklistmodelList);


           }

           @Override
           public void onError(Throwable e) {
               Log.e("bakrLOginng","onError");

           }
       });


     return value;
    }
    public void delete(Note note) {
         new deletenoteAscy(note_dao).execute(note);

    }
    public void update(Note note) {
        new updatenoteAscy(note_dao).execute(note);

    }
    public void deleteAll() {
        new deleteAllAsycn(note_dao).execute();

    }
    public void Update_AllNOtificationNotes(  ) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                note_dao.Update_AllNOtificationNotes();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.e("bakrLOginng","onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("bakrLOginng","Update_AllNOtificationNotes");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bakrLOginng","onError");

                    }
                });



    }
    public void Update_AllRemindersNotes(  ) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                note_dao.Update_AllRemindersNotes();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.e("bakrLOginng","onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("bakrLOginng","Update_AllNOtificationNotes");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bakrLOginng","onError");

                    }
                });



    }
    public LiveData<List<Note>>getAllnotes() {
       return  allnotes;
    }
    public LiveData< List<Integer>>   getlastrow() {
        LiveData< List<Integer>> values =note_dao.getlastrow();
        return  values;
    }
    public LiveData<List<checklistmodel>>getAllchecklistwhereID(int checklistID) {
        allchecklistmodel=note_dao.getAllchecklist_ID(checklistID);
         return  allchecklistmodel;
    }
    public void insert_checklist(final List<checklistmodel> checklistmodelList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                note_dao.insert_checklist(checklistmodelList);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.e("bakrLOginng","onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("bakrLOginng","onComplete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bakrLOginng","onError");

                    }
                });



    }
    public void update_checklist(final checklistmodel checklistmodel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                note_dao.update_checklist(checklistmodel);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.e("bakrLOginng","onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("bakrLOginng","onComplete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bakrLOginng","onError");

                    }
                });



    }
    public void delete_checklist(final checklistmodel checklistmodel) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                note_dao.delete_checklist(checklistmodel);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        Log.e("bakrLOginng","onSubscribe");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("bakrLOginng","onComplete");

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("bakrLOginng","onError");

                    }
                });



    }
    public List<Note>GetNotifcationAndReminderNotes() {
        List<Note>notficRemidNotes =new ArrayList<>();
        notficRemidNotes=note_dao.GetNotifcationAndReminderNotes();
        Log.e("GetNxzkl", "size"+notficRemidNotes.size() );
        return  notficRemidNotes;
    }



    private static class INsertnoteAscy extends AsyncTask<Note, Void ,Void>{
   private  Note_DAO Dao;

        public INsertnoteAscy(Note_DAO dao) {
            Dao = dao;
        }

        @Override
        protected Void doInBackground(Note... Notes) {
            Dao.insert_note(Notes[0]);
            return null;
        }
    }
    private static class deletenoteAscy extends AsyncTask<Note, Void ,Void>{
        private  Note_DAO Dao;

        public deletenoteAscy(Note_DAO dao) {
            Dao = dao;
        }

        @Override
        protected Void doInBackground(Note... Notes) {

            Dao.delete_note(Notes[0]);
            return null;
        }
    }
    private static class updatenoteAscy extends AsyncTask<Note, Void ,Void>{
        private  Note_DAO Dao;

        public updatenoteAscy(Note_DAO dao) {
            Dao = dao;
        }

        @Override
        protected Void doInBackground(Note... Notes) {

            Dao.update_note(Notes[0]);
            return null;
        }
    }
    private static class deleteAllAsycn extends AsyncTask<Void, Void ,Void>{
        private  Note_DAO Dao;

        public deleteAllAsycn(Note_DAO dao) {
            Dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Dao.Delete_AllNote();
            return null;
        }
    }




}
