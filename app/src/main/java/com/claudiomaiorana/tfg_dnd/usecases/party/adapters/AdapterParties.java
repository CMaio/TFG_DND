package com.claudiomaiorana.tfg_dnd.usecases.party.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;

public class AdapterParties extends RecyclerView.Adapter<AdapterParties.ViewHolder> implements View.OnClickListener {
    @Override
    public void onClick(View view) {

    }

    @NonNull
    @Override
    public AdapterParties.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterParties.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
                //listener.onItemClick(data.get(getAbsoluteAdapterPosition()));
            }else{
                //listener.onItemClick(data.get(getAbsoluteAdapterPosition()));
                //System.out.println(data.get(getAbsoluteAdapterPosition()) + " dddd");

            }
        }
    }

    public interface OnItemClickListener {

        void onItemClick(Character character);
    }
}
