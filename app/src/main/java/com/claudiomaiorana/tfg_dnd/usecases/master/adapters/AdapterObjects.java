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
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.model.Item;
import com.claudiomaiorana.tfg_dnd.model.Shield;
import com.claudiomaiorana.tfg_dnd.model.Usable;
import com.claudiomaiorana.tfg_dnd.model.Weapons;

import java.util.ArrayList;

public class AdapterObjects extends RecyclerView.Adapter<AdapterObjects.ViewHolder>{

    private ArrayList<Item> data;
    private Context context;
    private OnItemClickListener listener;

    public AdapterObjects(ArrayList<Item> data, Context context, OnItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_player_master_gameplay,parent,false);
        return new AdapterObjects.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = data.get(position);
        holder.txt_name.setText(item.getName());
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.rw_namePlayer_master_gameplay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAbsoluteAdapterPosition();
            if(clickedPosition == 0){
                listener.newObject();
            }else {

                switch (data.get(getAbsoluteAdapterPosition()).getType()){
                    case "weapons":
                        listener.onItemClick((Weapons) data.get(getAbsoluteAdapterPosition()));
                        break;
                    case "usables":
                        listener.onItemClick((Usable) data.get(getAbsoluteAdapterPosition()));
                        break;
                    case "shields":
                        listener.onItemClick((Shield) data.get(getAbsoluteAdapterPosition()));
                        break;
                    case "armors":
                        listener.onItemClick((Armor) data.get(getAbsoluteAdapterPosition()));
                        break;
                }

            }
        }
    }

    public interface OnItemClickListener {
        void newObject();
        void onItemClick(Weapons weapons);
        void onItemClick(Usable usable);
        void onItemClick(Shield shield);
        void onItemClick(Armor armor);
    }
}
