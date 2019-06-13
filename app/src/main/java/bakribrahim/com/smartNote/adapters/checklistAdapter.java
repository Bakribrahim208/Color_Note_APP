package bakribrahim.com.smartNote.adapters;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.models.checklistmodel;

public class checklistAdapter extends RecyclerView.Adapter<checklistAdapter.Note_ViewHoler> {
   List<checklistmodel> notelist = new ArrayList<checklistmodel>();
    private LayoutInflater layoutInflater;
    itemCHeck itemCHeck;
   Context con;
    public checklistAdapter() {


    }

    public void setClickListner(itemCHeck itemCHeck) {
        this.itemCHeck = itemCHeck;
    }

    @NonNull
    @Override
    public Note_ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View binding= inflater.inflate( R.layout.checklist_item,parent,false);
        return new Note_ViewHoler(binding);
    }

    public void setData(List<checklistmodel> checklistmodel) {
        this.notelist = checklistmodel;
        notifyDataSetChanged();

    }
  public List<checklistmodel> GetAllNotes(){
        return  notelist;

  }
    @Override
    public void onBindViewHolder(@NonNull Note_ViewHoler holder, int position) {


      if (notelist.get(position).isChecked())
      {

          holder.note.setText(setupText(notelist.get(position).getContent()));
          holder.checkBox.setChecked(true);
      }
      else
      {
          holder.note.setText(notelist.get(position).getContent());

      }
      holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

              if (isChecked)
                      {

                          holder.note.setText(setupText(notelist.get(position).getContent()));
                      }
                      else
                      {
                          holder.note.setText(notelist.get(position).getContent());

                      }
              checklistmodel tempchecklistmodel =notelist.get(position);
              tempchecklistmodel.setChecked(isChecked);
              itemCHeck.itemCHeck(tempchecklistmodel);


          }
      });
      }


    SpannableString  setupText(String str){
        SpannableString spannableString  =new SpannableString(str);
        StrikethroughSpan styleSpan =new StrikethroughSpan();
        spannableString.setSpan(styleSpan, 0,str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        return spannableString;
    }
    @Override
    public int getItemCount() {
        return notelist.size();
    }

    public class Note_ViewHoler extends RecyclerView.ViewHolder{
       // TextView titile , description ,priority ;
       CheckBox checkBox ;
        TextView note;
        public Note_ViewHoler(@NonNull View   itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkbox_);
            note=itemView.findViewById(R.id.daynmic_content);


         }
    }

    public interface itemCHeck{
        public void itemCHeck(checklistmodel checklistmodel);
    }




}
