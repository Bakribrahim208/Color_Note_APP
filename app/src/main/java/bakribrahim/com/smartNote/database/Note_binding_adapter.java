package bakribrahim.com.smartNote.database;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bakribrahim.com.smartNote.models.Note;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.adapters.RecycleviewAdapter;
import bakribrahim.com.smartNote.databinding.NoteBinding;

public class Note_binding_adapter extends RecyclerView.Adapter<Note_binding_adapter.Note_ViewHoler> {
    List<Note> notelist = new ArrayList<>();
    private LayoutInflater layoutInflater;

    @NonNull
    @Override
    public Note_binding_adapter.Note_ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        NoteBinding binding= DataBindingUtil.inflate(inflater, R.layout.note,parent,false);

        return new Note_binding_adapter.Note_ViewHoler(binding);
    }

    public void setNotelist(List<Note> notelist) {
        this.notelist = notelist;
        notifyDataSetChanged();

    }
    public List<Note> GetAllNotes(){
        return  notelist;

    }
    @Override
    public void onBindViewHolder(@NonNull Note_binding_adapter.Note_ViewHoler holder, int position) {
         holder.binding.setNotoo(notelist.get(position));

    }



    @Override
    public int getItemCount() {
        return notelist.size();
    }

    public class Note_ViewHoler extends RecyclerView.ViewHolder{
         NoteBinding binding ;

        public Note_ViewHoler(@NonNull NoteBinding  itemView) {
            super(itemView.getRoot());
            this.binding=itemView;

        }


    }

    public interface ItemClick{
        public void OnitemClick(Note note);
    }

    RecycleviewAdapter.ItemClick clickListner ;
    public void setListner(RecycleviewAdapter.ItemClick clickListner){
        this.clickListner=clickListner;
    }


}
