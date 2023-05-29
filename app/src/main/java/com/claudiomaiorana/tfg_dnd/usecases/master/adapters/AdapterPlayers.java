package com.claudiomaiorana.tfg_dnd.usecases.master.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;

import java.util.ArrayList;

public class AdapterPlayers extends RecyclerView.Adapter<AdapterPlayers.ViewHolder>{

    private ArrayList<Character> data;
    private Context context;
    private OnItemClickListener listener;

    public AdapterPlayers(ArrayList<Character> data, Context context, OnItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_master_gameplay,parent,false);
        return new AdapterPlayers.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Character character = data.get(position);
        holder.txt_name.setText(character.getName());
        holder.txt_Life.setText(Integer.toString(character.getCurrentHitPoints()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_name;
        TextView txt_Life;

        //TODO:Incluir todos los elementos de una fila
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.rw_namePlayer_master_gameplay);
            txt_Life = itemView.findViewById(R.id.rw_life_master_gameplay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(data.get(getAbsoluteAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Character character);
    }
}
