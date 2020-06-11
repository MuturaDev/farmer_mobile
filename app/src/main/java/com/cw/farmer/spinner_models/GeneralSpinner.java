package com.cw.farmer.spinner_models;

public class GeneralSpinner {

        int id;
        String shtDesc;


    public GeneralSpinner(int id, String shtDesc) {
        this.id = id;
        this.shtDesc = shtDesc;

    }

    @Override
    public String toString() {
        return shtDesc;
    }

    public int getId() {
        return id;
    }

    public String getShtDesc() {
        return shtDesc;
    }
}
