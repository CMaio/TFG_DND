package com.claudiomaiorana.tfg_dnd.usecases.character.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;

import java.util.ArrayList;

public class AdapterSkills extends RecyclerView.Adapter<AdapterSkills.ViewHolder> {

    private ArrayList<String> data;
    private Context context;
    private ArrayList<CheckBox> ck;

    public AdapterSkills(ArrayList<String> data, Context context) {
        this.data = data;
        this.context = context;
        ck = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_skill_element,parent,false);

        return new AdapterSkills.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String current = data.get(position);
        holder.chx_.setText(current);
        holder.chx_.setTag(position);
        //ck.add(holder.chx_);
    }
    public ArrayList<CheckBox> allChecks (){
        return ck;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox chx_;

        //TODO:Incluir todos los elementos de una fila
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chx_ = itemView.findViewById(R.id.ck_skill);

            chx_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int position = getAbsoluteAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        if(chx_.isChecked()){
                            ck.add(chx_);
                        }else{
                            ck.remove(chx_);
                        }
                    }
                }
            });
        }


    }
}
