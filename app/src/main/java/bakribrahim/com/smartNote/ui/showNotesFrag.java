package bakribrahim.com.smartNote.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.adapters.RecycleviewAdapter;
import bakribrahim.com.smartNote.adapters.checklistAdapter;
import bakribrahim.com.smartNote.models.checklistmodel;
import bakribrahim.com.smartNote.util.util;
import bakribrahim.com.smartNote.viewmodel.NoteviewModel;
import bakribrahim.com.smartNote.viewmodel.newsViewModelFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class showNotesFrag extends Fragment implements RecycleviewAdapter.ItemClick , checklistAdapter.itemCHeck{
    TextView blankTextView;
    RecycleviewAdapter adapter ;
    StaggeredGridLayoutManager staggeredGridLayoutManager  ;
    LinearLayoutManager linearLayoutManager  ;
    RecyclerView mrecycleview ;
    NoteviewModel noteviewModel ;
    View view;
    ArrayList<Note>datasource =new ArrayList<>();
    boolean Linearlayout_show;
      String fragmentType ="";
     public showNotesFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Boolean them = preferences.getBoolean("ThemPreferance", false);
        if (them)
        {
            getActivity().setTheme(R.style.AppTheme_NoActionBar);


        }

          view =inflater.inflate(R.layout.fragment_show_notes, container, false);
        setHasOptionsMenu(true);

        fragmentType =getArguments().getString("fragmentType");
        blankTextView=view.findViewById(R.id.blankTextView);

        mrecycleview= view.findViewById(R.id.recycleview);
          staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
          linearLayoutManager =new LinearLayoutManager(getContext());
        mrecycleview.setLayoutManager(staggeredGridLayoutManager);
       // mrecycleview.setLayoutManager(new LinearLayoutManager(getContext()));
        mrecycleview.setHasFixedSize(true);


        noteviewModel = ViewModelProviders.of(this,new newsViewModelFactory(getActivity().getApplication(),fragmentType)).get(NoteviewModel.class);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Note note =adapter.GetAllNotes().get(viewHolder.getAdapterPosition());

                if (direction==ItemTouchHelper.RIGHT)
               {
                   if (fragmentType==util.ReminderNotes)
                   {
                       note.setReminder(util.Reminder_null);
                       note.setArchive(true);

                   }
                   if (fragmentType== util.ArchiveNotes)
                   {
                       note.setArchive(false);
                       Snackbar.make(view, "Note UnArchived ", Snackbar.LENGTH_LONG)
                               .setAction("UDON", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       note.setArchive(true);
                                       noteviewModel.update(note);
                                   }
                               }).show();

                   }
                   else if (fragmentType==util.trashNotes)
                   {
                       note.setTrash(false);
                       Snackbar.make(view, "Note UnTrash ", Snackbar.LENGTH_LONG)
                               .setAction("UDON", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       note.setTrash(true);
                                       noteviewModel.update(note);
                                   }
                               }).show();

                   }
                   else if (fragmentType==util.CHECKlist)
                   {
                       note.setArchive(true);
                       Snackbar.make(view, "Note Archived ", Snackbar.LENGTH_LONG)
                               .setAction("UDON", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       note.setArchive(false);
                                       noteviewModel.update(note);
                                   }
                               }).show();
                   }
                   else if(fragmentType==util.favouriteeNotes){

                       note.setFavourite(false);
                       Snackbar.make(view, "Note Unfavourite ", Snackbar.LENGTH_LONG)
                               .setAction("UDON", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       note.setFavourite(false);
                                       noteviewModel.update(note);
                                   }
                               }).show();

                                       }
                   else if (fragmentType==util.Allnotes)
                   {
                       note.setArchive(true);
                       Snackbar.make(view, "Note Trashed", Snackbar.LENGTH_LONG)
                               .setAction("UDON", new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
                                       note.setArchive(false);
                                       noteviewModel.update(note);
                                   }
                               }).show();

                   }

               }

              else if (direction==ItemTouchHelper.LEFT)
                {
                    if (fragmentType==util.ReminderNotes)
                    {
                        note.setReminder(util.Reminder_null);
                        note.setTrash(true);

                    }
                    if (fragmentType==util.trashNotes)
                    {
                        show_snakBar(note);

                    }
                    else if (fragmentType==util.ArchiveNotes)
                    {
                        note.setTrash(true);
                        note.setArchive(false);
                        Snackbar.make(view, "Note Trashed", Snackbar.LENGTH_LONG)
                                .setAction("UDON", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        note.setArchive(true);
                                        note.setTrash(false);
                                        noteviewModel.update(note);
                                    }
                                }).show();

                    }
                    else if (fragmentType==util.CHECKlist)
                    {
                        note.setTrash(true);
                        Snackbar.make(view, "Note Trashed", Snackbar.LENGTH_LONG)
                                .setAction("UDON", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        note.setTrash(false);
                                        noteviewModel.update(note);
                                    }
                                }).show();

                    }
                    else if (fragmentType==util.favouriteeNotes)
                    {
                        note.setFavourite(false);
                        note.setTrash(true);
                        Snackbar.make(view, "Note Trashed", Snackbar.LENGTH_LONG)
                                .setAction("UDON", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        note.setFavourite(true);
                                        note.setTrash(false);
                                        noteviewModel.update(note);
                                    }
                                }).show();

                    }
                    else if (fragmentType==util.Allnotes)
                    {
                         note.setTrash(true);
                        Snackbar.make(view, "Note Trashed", Snackbar.LENGTH_LONG)
                                .setAction("UDON", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                         note.setTrash(false);
                                        noteviewModel.update(note);
                                    }
                                }).show();

                    }


                }

                noteviewModel.update(note);

            }
        })
                .attachToRecyclerView(mrecycleview);







        noteviewModel.getallnotes().observe((LifecycleOwner) this,
                new Observer<List<Note>>() {
                    @Override
                    public void onChanged(List<Note> Notes) {
                        if (Notes.size()<1)
                            blankTextView.setVisibility(View.VISIBLE);
                        else
                            blankTextView.setVisibility(View.GONE);
                         adapter =new RecycleviewAdapter(getContext(),Notes,getActivity().getApplication(),getActivity(),showNotesFrag.this::itemCHeck);
                        mrecycleview.setAdapter(adapter);
                        adapter.setClickListner(showNotesFrag.this);
                    }
                });

        noteviewModel.getchecklistwhereid(0).observe(this, new Observer<List<checklistmodel>>() {
            @Override
            public void onChanged(List<checklistmodel> checklistmodelList) {

            }
        });
        return view ;


    }

    @Override
    public void onPause() {
        super.onPause();
      }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         if (fragmentType.equals(util.trashNotes))
             getActivity().getMenuInflater().inflate(R.menu.menu_trash, menu);

         else if (fragmentType.equals(util.ArchiveNotes))
             getActivity().getMenuInflater().inflate(R.menu.menu_archive, menu);
         else
        getActivity().getMenuInflater().inflate(R.menu.main2, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id==R.id.ic_deleteall)
        {
            DeleteTrashNotes();
        }
        else if (id==R.id.ic_trashAll)
        {
            TrashAllNotes();
        }


        return super.onOptionsItemSelected(item);
    }
   public void  TrashAllNotes()
   {
       AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
       dialog.setMessage(R.string.confirm_trashNote_message)
               .setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       datasource.addAll(adapter.GetAllNotes());
                       for (int i =0 ; i<datasource.size();i++)
                       {
                           datasource.get(i).setTrash(true);
                           datasource.get(i).setArchive(false);
                           noteviewModel.update(datasource.get(i));
                       }

                       Toast.makeText(getContext(), "size"+adapter.GetAllNotes().size(),
                               Toast.LENGTH_SHORT).show();                    }
               })
               .setNegativeButton(R.string.str_cancle, new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               })
               .show();

   }
    public void DeleteTrashNotes() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(R.string.confirm_cleartrashNote_message)
                .setPositiveButton(R.string.str_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        datasource.addAll(adapter.GetAllNotes());
                        for (int i =0 ; i<datasource.size();i++)
                            noteviewModel.delete(datasource.get(i));
                        Toast.makeText(getContext(), "size"+adapter.GetAllNotes().size(),
                                Toast.LENGTH_SHORT).show();                    }
                })
                .setNegativeButton(R.string.str_cancle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    //
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        if (resultCode !=RESULT_CANCELED)
//        {
//            String stitle =data.getStringExtra(addNote.Extra_title);
//            String sdesc =data.getStringExtra(addNote.Extra_description);
//            int pro= data.getIntExtra(addNote.Extra_proirity,0);
//            Note note = (Note) data.getSerializableExtra(util.Note_Extra);
//            if (requestCode== ADD_Result_code &&resultCode==getActivity().RESULT_OK)
//            {
//                Note newnote= new Note(stitle,sdesc,pro,false ,false,false,false,false, util.Reminder_null,util.DefalutColor);
//                noteviewModel.insert(newnote,null);
//                Toast.makeText(getContext(), "successfully added", Toast.LENGTH_SHORT).show();
//            }
//            else if (requestCode== Edite_Result_code &&resultCode==getActivity().RESULT_OK)
//            {
//                int Id= data.getIntExtra(addNote.Extra_ID,-1);
//
//                if (Id!=-1)
//                {
//
//                    Note newnote= new Note(stitle,sdesc,pro,false ,false,false,false,false, util.Reminder_null,util.DefalutColor);
//                    newnote.setID(Id);
//                    noteviewModel.update(note);
//                    Toast.makeText(getContext(), "successfully updated", Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        }
//
//    }


    public void show_snakBar(Note note) {
        Snackbar snackbar = Snackbar
                .make(view.getRootView(), "you sure to Delete this Note ?", Snackbar.LENGTH_LONG)
                .setAction("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (note.isIschecklist())
                        {
                            noteviewModel.getchecklistwhereid(0).observe(showNotesFrag.this, new Observer<List<checklistmodel>>() {
                                @Override
                                public void onChanged(List<checklistmodel> checklistmodelList) {
                                    for (int i=0 ; i<checklistmodelList.size();i++)
                                        noteviewModel.Delete_checklist(checklistmodelList.get(i));                                }
                            });
                            Toast.makeText(getContext(), "deleted isIschecklist successfully", Toast.LENGTH_SHORT).show();

                        }
                        noteviewModel.delete(note);
                        Toast.makeText(getContext(), "deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });
        snackbar.setActionTextColor(R.attr.textcolor);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(R.attr.backgroundcolor);
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(R.attr.textcolor);
        snackbar.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
         noteviewModel.getallnotes().removeObservers(getActivity());
    }

    @Override
    public void OnitemClick(Note note) {

        // code for view and update note
        if (note.isIschecklist())
        {
            Intent intent =new Intent(getActivity(),add_checklistActicity.class);
            intent.putExtra("note", note);
            startActivity(intent);
        }
        else
        {
            Intent intent =new Intent(getActivity(),addNotetest.class);
            intent.putExtra("note", note);
             startActivity(intent);
        }

    }

    @Override
    public List<checklistmodel> getcheclistdata(int NoteID) {
        Log.e("message", "getcheclistdata");
          return noteviewModel.getchecklistwhereid(NoteID).getValue();
    }

    @Override
    public void itemCHeck(checklistmodel checklistmodel) {
        noteviewModel.update_checklist(checklistmodel);
    }
}
