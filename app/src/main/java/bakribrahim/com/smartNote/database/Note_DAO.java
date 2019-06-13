package bakribrahim.com.smartNote.database;


import java.util.List;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.models.checklistmodel;

@Dao
public interface Note_DAO {


     @Insert
    long insert_note(Note newnote);
    @Update
    void update_note (Note newnote);
    @Delete
    void delete_note (Note newnote);

    @Query("Delete  from NoteTable")
    void Delete_AllNote ();


    //clear all notification that appear in screen in setting actviity
    @Query(" UPDATE NoteTable SET notifcation=0 WHERE notifcation=1 ")
    void Update_AllNOtificationNotes ();
    //clear all notification that appear in screen in setting actviity
    @Query(" UPDATE NoteTable SET reminder=0 WHERE reminder=1 ")
    void Update_AllRemindersNotes ();



    @Insert
    void insert_checklist(List<checklistmodel> checklistmodelList);

    @Update
    void update_checklist (checklistmodel checklistmodel);
    @Delete
    void delete_checklist (checklistmodel checklistmodel);

    @Query("Delete  from checklisttable")
    void DeleteALL_checklist ();

    @Query("Select * from checklisttable WHERE NoteID in(:noteid) ")
    LiveData<List<checklistmodel>> getAllchecklist_ID (int noteid);


    @Query("Select ID from notetable ORDER by ID DESC LIMIT 1  ")
    LiveData< List<Integer>>  getlastrow ();

    @Query("Select * from NoteTable WHERE archive = 0 AND trash = 0  ORDER by priority DESC")
    LiveData<List<Note>> GetAllNote ();

    @Query("Select * from NoteTable WHERE reminder = 1 OR notifcation=1  ")
    List<Note> GetNotifcationAndReminderNotes ();

    @Query("Select * from NoteTable WHERE archive = 0 AND trash =1  ORDER by priority DESC")
    LiveData<List<Note>> GetTrashNotes ();

    @Query("Select * from NoteTable WHERE archive = 1 AND trash =0  ORDER by priority DESC")
    LiveData<List<Note>> GetArchiveNotes ();

    @Query("Select * from NoteTable WHERE favourite = 1  AND archive = 0 AND trash = 0 ORDER by priority DESC")
    LiveData<List<Note>> GetFavoriteeNotes ();

    @Query("Select * from NoteTable WHERE ischecklist = 1 AND archive = 0 AND trash = 0   ORDER by priority DESC")
    LiveData<List<Note>> GetchecklistNotes ();

    @Query("Select * from NoteTable WHERE reminder NOT LIKE :reminderempty ")
    LiveData<List<Note>> GetReminders (String reminderempty);

}
