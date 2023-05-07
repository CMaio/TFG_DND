package com.claudiomaiorana.tfg_dnd.model;

import java.io.Serializable;

public class RCAInfo implements Serializable {

    private String tittleText;
    private String imageAdd;
    private String codeApiSearch;


    public RCAInfo(){}

    public RCAInfo(String name,String imageAdd,String code){
        this.tittleText = name;
        this.imageAdd = imageAdd;
        this.codeApiSearch = code;
    }

    public String getTittleText() {
        return tittleText;
    }

    public String getImageAdd() {
        return imageAdd;
    }

    public String getCodeApiSearch() {
        return codeApiSearch;
    }
}
