package com.claudiomaiorana.tfg_dnd.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOError;

public class PopUpCustom extends DialogFragment {
    View v;
    String tittle,message,yesButtonText, noButtonText;
    Boolean yesButton, noButton;
    private IDialogListener listener;

    /*lo que me apetece controlar, titulo, mensaje, view,tipopregunta, inlcuso si se ve botones de aceptar y de cancelar*/
    public PopUpCustom(View view){
        this.v = view;
        this.tittle = "";
        this.message = "";
        this.yesButtonText = "";
        this.noButtonText = "";
        this.yesButton = false;
        this.noButton = false;
    }

    public PopUpCustom(View view, String tittle,String message,String yesButtonText,String noButtonText){
        this.v = view;
        this.tittle = tittle;
        this.message = message;
        this.yesButtonText = yesButtonText;
        this.noButtonText = noButtonText;
        this.yesButton = (!yesButtonText.equals("")) && yesButtonText != null? true : false;
        this.noButton = (!noButtonText.equals("")) && noButtonText != null? true : false;
    }


    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        /* aqui poner if de los elementos !tittle.equal("") para que lo ponga en caso de que este, sino no lo pone*/

        builder.setView(v);

        if(!tittle.equals("")){builder.setTitle(tittle);}
        if(!message.equals("")){builder.setMessage(message);}

        if(yesButton){
            builder.setPositiveButton(yesButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Aqui hay que hacer algo:TODO

                }
            });
        }

        if(noButton){
            builder.setNegativeButton(noButtonText, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //aqui hay que hacer algo:TODO
                }
            });
        }


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
            listener = (IDialogListener) context;
        }catch (Exception e){

        }
    }
}
