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
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.util.Constants;

import java.util.ArrayList;

public class AdapterCharacters extends RecyclerView.Adapter<AdapterCharacters.ViewHolder> implements View.OnClickListener {

    private ArrayList<Character> data;
    private Context context;
    private OnItemClickListener listener;


    public AdapterCharacters(ArrayList<Character> data, OnItemClickListener listener, Context context) {
        this.data = data;
        this.listener =listener;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterCharacters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        System.out.println(viewType + " aqui estamos------------------------------hasta aqui");
        switch(viewType){
            case Constants.TYPE_CREATE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_new_character, parent, false);
                return new AdapterCharacters.ViewHolder(view);

            case Constants.TYPE_FILLED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_character, parent, false);
                return new AdapterCharacters.ViewHolder(view);
        }
        return new AdapterCharacters.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCharacters.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case Constants.TYPE_CREATE:

                break;
            case Constants.TYPE_FILLED:
                System.out.println("------------------------------------------d");
                Character current = data.get(position);
                //TODO: Llenar con todo lo necesario de una linea

                /*Glide.with(context).load(current.getUrlImagePreview()).into(holder.iv_image);*/ //para cargar la imagen

                holder.rtv_name.setText(current.getName());
                holder.rtv_gender.setText(current.getGender());
                holder.rtv_pronoun.setText(current.getPronoun());
                holder.rtv_class.setText(current.getClassPlayer());
                holder.rtv_race.setText(current.getRace());
                holder.rtv_level.setText(String.valueOf(current.getLevel()));
                break;
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

    @Override
    public void onClick(View view) {
        System.out.println("hola");
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView rtv_name,rtv_pronoun,rtv_gender,rtv_level,rtv_race,rtv_class;
        ImageView iv_character;

        //TODO:Incluir todos los elementos de una fila
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            setComponents(itemView);

            //Esto establece la linea como boton
            itemView.setOnClickListener(this);
        }


        private void setComponents(View itemView){
            rtv_name = itemView.findViewById(R.id.rtv_name);
            rtv_pronoun = itemView.findViewById(R.id.rtv_ponoun);
            rtv_gender = itemView.findViewById(R.id.rtv_gender);
            rtv_level = itemView.findViewById(R.id.rtv_level);
            rtv_race = itemView.findViewById(R.id.rtv_race);
            rtv_class = itemView.findViewById(R.id.rtv_class);
            iv_character = itemView.findViewById(R.id.IV_state_character);
        }

        @Override
        public void onClick(View v) {//iniciem el detall del cocktail seleccionat
            int clickedPosition = getAbsoluteAdapterPosition();
            if(clickedPosition == 0){
                listener.onItemClick(data.get(getAbsoluteAdapterPosition()));
            }else{
                listener.onItemClick(data.get(getAbsoluteAdapterPosition()));
                System.out.println(data.get(getAbsoluteAdapterPosition()) + " dddd");

            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Character character);
    }
}
