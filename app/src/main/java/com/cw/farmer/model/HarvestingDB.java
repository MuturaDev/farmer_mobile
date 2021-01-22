package com.cw.farmer.model;

import com.orm.SugarRecord;

import java.io.Serializable;

public class HarvestingDB extends SugarRecord<HarvestingDB> implements Serializable {

    public String dateid;
    public String noofunits;
    public String farmerid;
    public String harvestkilos;
    public String locale;
    public String contractId;
    public String farmerName;


    public HarvestingDB() {
    }

    public HarvestingDB(String dateid, String noofunits, String farmerid, String harvestkilos, String locale, String contractId, String farmerName) {
        this.dateid = dateid;
        this.noofunits = noofunits;
        this.farmerid = farmerid;
        this.harvestkilos = harvestkilos;
        this.locale = locale;
        this.contractId = contractId;
        this.farmerName = farmerName;
    }
}