package bakribrahim.com.smartNote.models;


import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "NoteTable")
public class Note implements Serializable {
    @PrimaryKey (autoGenerate = true)
    private  int ID;
    private  String title;
    private  String description ;
     private  int priority;
    boolean archive ;

    public boolean isIschecklist() {
        return ischecklist;
    }

    public void setIschecklist(boolean ischecklist) {
        this.ischecklist = ischecklist;
    }

    boolean trash;
    boolean favourite;
    boolean notifcation;
    boolean ischecklist;
    String reminder;
    int  backgroundColor;
    public Note()
    {

    }
    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public Note(String title, String description, int priority , boolean trash
            , boolean archive, boolean favourite
             , boolean notifcation ,boolean ischecklist, String reminder,int  backgroundColor) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.trash =trash ;
        this.archive =archive;
        this.favourite=favourite;
        this.notifcation=notifcation;
        this.reminder=reminder;
        this.backgroundColor=backgroundColor;
        this.ischecklist =ischecklist;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getReminder() {
        return reminder;
    }

    public void setNotifcation(boolean notifcation) {
        this.notifcation = notifcation;
    }

    public boolean isNotifcation() {
        return notifcation;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public void setTrash(boolean trash) {
        this.trash = trash;
    }

    public boolean isArchive() {
        return archive;
    }

    public boolean isTrash() {
        return trash;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    @NonNull
    @Override
    public String toString() {
        return "getID\t"+this.getID() +"\n"+
                "getTitle\t"+this.getTitle() +"\n"+
                "getDescription\t"+this.getDescription() +"\n"+
                "getReminder\t"+this.getReminder() +"\n"+
                "getPriority\t"+this.getPriority() +"\n"+
                "isNotifcation\t"+this.isNotifcation()+"\n"+
                "isIschecklist\t"+this.isIschecklist()+"\n"+
                "isTrash\t"+this.isTrash() +"\n"+
                "isArchive\t"+this.isArchive() +"\n"+
                "isFavourite\t"+this.isFavourite() +"\n"
                +"getBackgroundColor\t"+this.getBackgroundColor() +"\n"

                ;
    }
}
