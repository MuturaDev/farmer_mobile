package com.cw.farmer.model;

public class FarmNamesResponse {


    int id;
    String shtDesc;
    String desc;

    public FarmNamesResponse(int id, String shtDesc, String desc) {
        this.id = id;
        this.shtDesc = shtDesc;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShtDesc() {
        return shtDesc;
    }

    public void setShtDesc(String shtDesc) {
        this.shtDesc = shtDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
