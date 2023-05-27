package com.claudiomaiorana.tfg_dnd.usecases.game.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.usecases.character.adapters.AdapterObjects;

import java.util.ArrayList;

public class AdapterItemsGame extends RecyclerView.Adapter<AdapterItemsGame.ViewHolder> {

    private ArrayList<String> data;
    private Context context;
    private OnItemClickListener listener;

    public AdapterItemsGame(ArrayList<String> data, Context context, OnItemClickListener listener) {
        this.data = data;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdapterItemsGame.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_quantity_spell,parent,false);

        return new AdapterItemsGame.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItemsGame.ViewHolder holder, int position) {
        String nameObject = data.get(position);
        holder.txt_LevelQuantity.setText(nameObject);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_LevelQuantity;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_LevelQuantity = itemView.findViewById(R.id.rw_qt_spell);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(data.get(getAbsoluteAdapterPosition()));

        }
    }

    public interface OnItemClickListener {
        void onItemClick(String nameObject);
    }
}

