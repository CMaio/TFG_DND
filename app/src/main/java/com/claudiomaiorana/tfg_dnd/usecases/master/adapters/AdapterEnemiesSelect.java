package com.claudiomaiorana.tfg_dnd.usecases.master.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.EnemySelector;

import java.util.ArrayList;


public class AdapterEnemiesSelect extends RecyclerView.Adapter<AdapterEnemiesSelect.ViewHolder>{

    private ArrayList<EnemySelector> data;
    private Context context;

    public AdapterEnemiesSelect(ArrayList<EnemySelector> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_enemy_element,parent,false);

        return new AdapterEnemiesSelect.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EnemySelector current = data.get(position);

        holder.name.setText(current.getName());
        holder.life.setText(String.valueOf(current.getHitPoints()));
        holder.quantityEnemies.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String userValueString = holder.quantityEnemies.getText().toString();
                int userValue = Integer.parseInt(userValueString);
                current.setUserValue(userValue);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<EnemySelector> getData(){return data;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView life;
        EditText quantityEnemies;

        //TODO:Incluir todos los elementos de una fila
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.rtx_nameEnemy);
            life = itemView.findViewById(R.id.rtx_lifeEnemy);
            quantityEnemies = itemView.findViewById(R.id.ertx_quantity);

        }
    }

}
