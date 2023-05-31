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
import com.claudiomaiorana.tfg_dnd.model.Shield;
import com.claudiomaiorana.tfg_dnd.model.Usable;
import com.claudiomaiorana.tfg_dnd.model.Weapons;

import java.util.ArrayList;

public class AdapterAttacksEnemy extends RecyclerView.Adapter<AdapterAttacksEnemy.ViewHolder>{

    private ArrayList<Weapons> data;
    private Context context;

    public AdapterAttacksEnemy(ArrayList<Weapons> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_master_gameplay,parent,false);
        return new AdapterAttacksEnemy.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weapons weapons = data.get(position);
        holder.txt_name.setText(weapons.getName());
        holder.txt_hit.setText(weapons.getDamageDice());
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name;
        TextView txt_hit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.rw_namePlayer_master_gameplay);
            txt_hit = itemView.findViewById(R.id.rw_life_master_gameplay);
        }
    }
}
