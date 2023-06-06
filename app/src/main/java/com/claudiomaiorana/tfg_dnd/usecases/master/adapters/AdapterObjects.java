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
import com.claudiomaiorana.tfg_dnd.model.Item;
import com.claudiomaiorana.tfg_dnd.util.Constants;

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
        if(holder.getItemViewType() == Constants.TYPE_CREATE){
            holder.txt_name.setText(context.getResources().getText(R.string.createItem));
            holder.txt_type.setText("");
        }else if(holder.getItemViewType() == Constants.TYPE_FILLED){
            Item item = data.get(position);
            holder.txt_name.setText(item.getName());
            holder.txt_type.setText("("+item.getType()+")");
        }

    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return Constants.TYPE_CREATE;
        } else {
            return Constants.TYPE_FILLED;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_name;
        TextView txt_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.rw_namePlayer_master_gameplay);
            txt_type = itemView.findViewById(R.id.rw_life_master_gameplay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAbsoluteAdapterPosition();
            if(clickedPosition == 0){
                listener.newObject();
            }else {

                listener.onItemClick(data.get(getAbsoluteAdapterPosition()));

            }
        }
    }



    public interface OnItemClickListener {
        void newObject();
        void onItemClick(Item item);
    }
}
