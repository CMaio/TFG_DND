package com.claudiomaiorana.tfg_dnd.usecases.master.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.OptionsCharacter;

import java.util.ArrayList;

public class AdapterItemsSelect extends RecyclerView.Adapter<AdapterItemsSelect.ViewHolder>{

    private ArrayList<OptionsCharacter> data;
    private Context context;



    public AdapterItemsSelect(ArrayList<OptionsCharacter> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_skill_element,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,int position) {
        OptionsCharacter current = data.get(position);

        holder.chx_.setText(current.getName());
        holder.chx_.setOnCheckedChangeListener(null);
        holder.chx_.setChecked(data.get(position).isSelected());
        holder.chx_.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                data.get(holder.getAbsoluteAdapterPosition()).setSelected(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<OptionsCharacter> getData(){return data;}



    public class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox chx_;

        //TODO:Incluir todos los elementos de una fila
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chx_ = itemView.findViewById(R.id.ck_skill);

        }
    }

}
