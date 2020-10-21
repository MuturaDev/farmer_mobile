package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class ChangeCentreTB extends SugarRecord<ChangeCentreTB> {

    private String activate;
    private String farmerId;
    private String centerId;

    public ChangeCentreTB(String activate, String farmerId, String centerId) {
        this.activate = activate;
        this.farmerId = farmerId;
        this.centerId = centerId;
    }

    public ChangeCentreTB() {
    }

    public String getActivate() {
        return activate;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public String getCenterId() {
        return centerId;
    }
}
