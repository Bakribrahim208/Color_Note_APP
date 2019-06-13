package bakribrahim.com.smartNote.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import androidx.annotation.NonNull;
 import androidx.lifecycle.LiveData;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.models.checklistmodel;
import bakribrahim.com.smartNote.database.Repoistary;

public class NoteviewModel extends AndroidViewModel {
       Repoistary repoistary ;
       LiveData<List<Note>>allnotes;

    public NoteviewModel(@NonNull Application application ,String type ) {
        super(application);

        repoistary = new Repoistary(application,type);
         allnotes = repoistary.getAllnotes();



    }

    public long insert(Note note , List<checklistmodel>checklist){
      return  repoistary.insert(note,checklist);
    }
    public void delete(Note note){
        repoistary.delete(note);
    }
    public void update(Note note){
        repoistary.update(note);
    }
    public void deleteAll(){
        repoistary.deleteAll();
    }


    public void Update_AllNOtificationNotes( )
    {
        repoistary.Update_AllNOtificationNotes();
    }
    public void Update_AllRemindersNotes( )
    {
        repoistary.Update_AllRemindersNotes();
    }




    public LiveData<List<Note>> getallnotes(){
        return  allnotes;
    }
    public LiveData<List<checklistmodel>> getchecklistwhereid(int checklistID){
        return  repoistary.getAllchecklistwhereID(checklistID);
    }

    public LiveData< List<Integer>>   getlastRow(){
        return  repoistary.getlastrow();
    }

   public void update_checklist(checklistmodel checklist)
{
    repoistary.update_checklist(checklist);
}
    public void insert_checklist(List<checklistmodel>checklist){
          repoistary.insert_checklist(checklist);
    }

    public void Delete_checklist(checklistmodel checklistmodel){
        repoistary.delete_checklist(checklistmodel);
    }
}
