package com.cw.farmer.spinner_models;

public class FarmSpinner {

        int id;
        String shtDesc;


    public FarmSpinner(int id, String shtDesc) {
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
