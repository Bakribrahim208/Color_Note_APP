package bakribrahim.com.smartNote.adapters;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.databinding.NoteBinding;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.models.checklistmodel;
import bakribrahim.com.smartNote.util.util;
import bakribrahim.com.smartNote.viewmodel.NoteviewModel;
import bakribrahim.com.smartNote.viewmodel.newsViewModelFactory;

public class RecycleviewAdapter extends RecyclerView.Adapter<RecycleviewAdapter.Note_ViewHoler> {
    List<Note> notelist = new ArrayList<>();
    private LayoutInflater layoutInflater;
    ItemClick clickListner ;
    Application application;
    LifecycleOwner lifecycle ;
    Context con;
checklistAdapter.itemCHeck itemCHeck;
    public RecycleviewAdapter(Context con , List<Note> notelist , Application application, LifecycleOwner lifecycle, checklistAdapter.itemCHeck itemCHeck) {
        this.con = con;
        this.application=application;
        this.lifecycle =lifecycle;
        this.itemCHeck=itemCHeck;
        this.notelist=notelist;
    }

    public void setClickListner(ItemClick clickListner) {
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public Note_ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        NoteBinding binding= DataBindingUtil.inflate(inflater, R.layout.note,parent,false);

        return new Note_ViewHoler(binding);
    }

    public void setNotelist(List<Note> notelist) {


        this.notelist = notelist;
        notifyDataSetChanged();

    }
  public List<Note> GetAllNotes(){
        return  notelist;

  }
    private final static int FADE_DURATION = 1000; //FADE_DURATION in milliseconds

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    @Override
    public void onBindViewHolder(@NonNull Note_ViewHoler holder, int position) {
        setFadeAnimation(holder.itemView);
        if (notelist.get(position).getBackgroundColor()!= util.DefalutColor)
            holder.binding.noteCard.setBackgroundColor(notelist.get(position).getBackgroundColor());

        holder.binding.setNotoo(notelist.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListner.OnitemClick(notelist.get(position));
            }
        });

        if (notelist.get(position).isIschecklist())
        {
            holder.binding.txtDescription.setVisibility(View.GONE);
            holder.binding.txtReminder.setVisibility(View.GONE);
             holder.binding.recycleview.setVisibility(View.VISIBLE);
            checklistAdapter checklistAdapter =new checklistAdapter();
            holder.binding.recycleview.setAdapter(checklistAdapter);
            RecyclerView.LayoutManager layoutManager =new LinearLayoutManager(con);
            holder.binding.recycleview.setLayoutManager(layoutManager);
            checklistAdapter.setClickListner(itemCHeck);
            NoteviewModel   noteviewModel = ViewModelProviders.of((FragmentActivity) con,new newsViewModelFactory(application,util.CHECKlist)).get(NoteviewModel.class);
            noteviewModel.getchecklistwhereid(notelist.get(position).getID()).observe(lifecycle, new Observer<List<checklistmodel>>() {
                @Override
                public void onChanged(List<checklistmodel> checklistmodelList) {
                     checklistAdapter.setData(checklistmodelList);
                 }
            });

        }

      }



    @Override
    public int getItemCount() {
        return notelist.size();
    }

    public class Note_ViewHoler extends RecyclerView.ViewHolder{
       // TextView titile , description ,priority ;
       NoteBinding binding ;


        public Note_ViewHoler(@NonNull NoteBinding   itemView) {
            super(itemView.getRoot());
            this.binding =itemView;


         }
    }

    public interface ItemClick{
        public void OnitemClick(Note note);
        public List<checklistmodel> getcheclistdata(int NoteID);

    }




}
