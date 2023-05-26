package com.claudiomaiorana.tfg_dnd.usecases.character.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.claudiomaiorana.tfg_dnd.R;
import com.claudiomaiorana.tfg_dnd.model.Character;
import com.claudiomaiorana.tfg_dnd.util.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapterCharacters extends RecyclerView.Adapter<AdapterCharacters.ViewHolder>{

    private ArrayList<Character> data;
    private Context context;
    private OnItemClickListener listener;
    FirebaseStorage dbStorage = FirebaseStorage.getInstance();


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
                Character current = data.get(position);
                if(!current.getImgPlayerName().equals("")){
                    StorageReference storageRef = dbStorage.getReference();
                    String imageName = current.getImgPlayerName();
                    StorageReference imageRef = storageRef.child(imageName);

                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context)
                                    .load(uri)
                                    .into(holder.iv_character);
                        }
                    });
                }else{
                    Glide.with(context)
                            .load(R.drawable.avatar_1)
                            .into(holder.iv_character);
                }


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
                listener.newCharacter();
            }else{
                Drawable drawable = iv_character.getDrawable();
                Bitmap bitmap = null;
                if (drawable instanceof BitmapDrawable) {
                    bitmap = ((BitmapDrawable) drawable).getBitmap();
                }
                listener.selectCharacter(data.get(getAbsoluteAdapterPosition()),bitmap);
                //System.out.println(data.get(getAbsoluteAdapterPosition()) + " dddd");

            }
        }
    }

    public interface OnItemClickListener {

        void newCharacter();
        void selectCharacter(Character character, Bitmap bitmap);
    }
}
