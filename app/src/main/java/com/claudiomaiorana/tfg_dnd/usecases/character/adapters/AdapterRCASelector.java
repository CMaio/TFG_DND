package com.claudiomaiorana.tfg_dnd.usecases.character.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.RCAInfo;

import java.util.ArrayList;

public class AdapterRCASelector extends RecyclerView.Adapter<AdapterRCASelector.ViewHolder> {
    private Context context;
    private ArrayList<RCAInfo> type;
    private OnItemClickListener listener;


    public AdapterRCASelector(Context context, ArrayList<RCAInfo> type){
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_rca_element,parent,false);
        AdapterRCASelector.ViewHolder viewHolder = new AdapterRCASelector.ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRCASelector.ViewHolder holder, int position) {
        RCAInfo current = type.get(position);
        holder.txt_nameRCAElement.setText(current.getTittleText());
    }

    @Override
    public int getItemCount() {
        return type.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txt_nameRCAElement;
        ImageView img_profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_nameRCAElement = itemView.findViewById(R.id.txt_nameRCAElement);
            img_profile = itemView.findViewById(R.id.img_faceRCAElement);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(type.get(getAbsoluteAdapterPosition()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RCAInfo rcaInfo);
    }
}
