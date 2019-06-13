package bakribrahim.com.smartNote.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import bakribrahim.com.smartNote.util.util;

@Entity(tableName = "ChecklistTable")
public class checklistmodel {
    @PrimaryKey(autoGenerate = true)
    int ID ;
    int NoteID;

    public void setNoteID(int noteID) {
        NoteID = noteID;
    }

    public int getNoteID() {
        return NoteID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    String content ;

    public int getID() {
        return ID;
    }

    boolean checked ;
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isDeleted() {
        return deleted;
    }

    boolean deleted;
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public checklistmodel(int noteID,String content, boolean checked,boolean delete) {
        this.content = content;
        this.checked = checked;
        this.deleted =delete;
        this.NoteID =noteID;
    }
    public checklistmodel( ) {
    }
    @NonNull
    @Override
    public String toString() {

        return this.ID + util.item_Seperator+this.NoteID+util.item_Seperator+ content +util.item_Seperator + isChecked()+util.item_Seperator +this.isDeleted()+util.item_Seperator ;
    }
}
