package com.claudiomaiorana.tfg_dnd.usecases.character.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdapterSpellsQuantity extends RecyclerView.Adapter<AdapterSpellsQuantity.ViewHolder> {

    private Map<String,Integer> data;
    private Context context;
    List<String> keys;

    public AdapterSpellsQuantity(Map<String,Integer> data,Context context/*,OnItemClickListener listener*/){
        this.data = data;
        this.context = context;
        this.keys = new ArrayList<>(this.data.keySet());
        Collections.sort(this.keys);
    }

    @NonNull
    @Override
    public AdapterSpellsQuantity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quantity_spell,parent,false);

        return new AdapterSpellsQuantity.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 0 ){
            String key = keys.get(position);
            int value = data.get(key);
            key = key.substring(1);
            String text = key + ": "+value;
            holder.txt_LevelQuantity.setText(text);
        }else{
            String key = keys.get(position);
            int value = data.get(key);
            String text = "Lvl " +key + ": "+value;
            holder.txt_LevelQuantity.setText(text);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txt_LevelQuantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_LevelQuantity = itemView.findViewById(R.id.rw_qt_spell);

        }
    }
}
