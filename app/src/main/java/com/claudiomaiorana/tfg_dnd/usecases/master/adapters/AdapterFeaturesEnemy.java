package com.claudiomaiorana.tfg_dnd.usecases.master.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Armor;
import com.claudiomaiorana.tfg_dnd.model.Item;
import com.claudiomaiorana.tfg_dnd.model.ProfLang;
import com.claudiomaiorana.tfg_dnd.model.Shield;
import com.claudiomaiorana.tfg_dnd.model.Usable;
import com.claudiomaiorana.tfg_dnd.model.Weapons;

import java.util.ArrayList;

public class AdapterFeaturesEnemy extends RecyclerView.Adapter<AdapterFeaturesEnemy.ViewHolder>{

    private ArrayList<ProfLang> data;
    private Context context;

    public AdapterFeaturesEnemy(ArrayList<ProfLang> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_master_gameplay,parent,false);
        return new AdapterFeaturesEnemy.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProfLang profLang = data.get(position);
        holder.txt_name.setText(profLang.getName());
    }
    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.rw_namePlayer_master_gameplay);
        }

    }


}
