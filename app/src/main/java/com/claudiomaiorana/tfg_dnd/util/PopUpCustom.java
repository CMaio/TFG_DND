package com.claudiomaiorana.tfg_dnd.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOError;

public class PopUpCustom extends DialogFragment {
    View view;

    public PopUpCustom(/*lo que me apetece controlar, titulo, mensaje, view,tipopregunta, inlcuso si se ve botones de aceptar y de cancelar*/){

    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        /* aqui poner if de los elementos !tittle.equal("") para que lo ponga en caso de que este, sino no lo pone*/

        //builder.setView(view);

        builder.setPositiveButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return super.onCreateDialog(savedInstanceState);
    }

    public interface IDialogListener{
        void clickedYes(int question,View view);
        void clickedNo(int question);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            //listener = (IDialogListener) context;
        }catch (IOError e){
            //throw new Exception(e);
        }
    }
}
