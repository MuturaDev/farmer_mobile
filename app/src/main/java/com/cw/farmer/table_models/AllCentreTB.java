package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class AllCentreTB extends SugarRecord<AllCentreTB> {

    private Integer ID;
    private String name;
    private String centreType;

    public AllCentreTB() {
    }

    public AllCentreTB(Integer ID, String name, String centreType) {
        this.ID = ID;
        this.name = name;
        this.centreType = centreType;
    }

    public Integer getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getCentreType() {
        return centreType;
    }
}
