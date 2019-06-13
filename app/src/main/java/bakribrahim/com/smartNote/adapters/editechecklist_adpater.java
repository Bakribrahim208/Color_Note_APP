package bakribrahim.com.smartNote.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bakribrahim.com.smartNote.R;
import bakribrahim.com.smartNote.models.checklistmodel;

public class editechecklist_adpater extends RecyclerView.Adapter<editechecklist_adpater.Note_ViewHoler> {
   List<checklistmodel> notelist = new ArrayList<checklistmodel>();
    private LayoutInflater layoutInflater;
    itemCHeck itemCHeck;
      Context con;
    public editechecklist_adpater(itemCHeck itemCHeck) {
         this.itemCHeck=itemCHeck;

    }

    public void setClickListner(itemCHeck itemCHeck) {
        this.itemCHeck = itemCHeck;
    }

    @NonNull
    @Override
    public Note_ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View binding= inflater.inflate( R.layout.checklistitem_dynmic,parent,false);
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
          holder.note.setEnabled(false);
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
                    holder.note.setEnabled(false);
                    holder.note.setText(setupText(notelist.get(position).getContent()));

                }
                else
                {
                    holder.note.setEnabled(true);
                    holder.note.setText(notelist.get(position).getContent());

                }

                notelist.get(position).setChecked(isChecked);
            }
        });



      holder.note.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (!hasFocus) {
                        holder.delete_.setVisibility(View.GONE);
                    }else{
                        holder.delete_.setVisibility(View.VISIBLE);
                     }

                }
            });
            holder.note.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                     notelist.get(position).setContent(s.toString());

                }
            });
            holder.delete_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemCHeck.Deletechlistmodel(notelist.get(position));
                    notelist.remove(position);
                    notifyDataSetChanged();
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
        EditText note ;
        ImageView delete_;
        CheckBox checkBox;
        public Note_ViewHoler(@NonNull View   itemView) {
            super(itemView);
            note =itemView.findViewById(R.id.daynmic_content);
              delete_=itemView.findViewById(R.id.delete_view);
              checkBox=itemView.findViewById(R.id.checkbox_);


        }
    }

    public interface itemCHeck{
        public void itemCHeck(checklistmodel checklistmodel);
        public void Deletechlistmodel(checklistmodel checklistmodel);

    }




}
