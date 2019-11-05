package com.cw.farmer.model;

import com.orm.SugarRecord;

public class HarvestingDB extends SugarRecord<HarvestingDB> {

    public String dateid;
    public String noofunits;
    public String farmerid;
    public String harvestkilos;
    public String locale;


    public HarvestingDB() {
    }

    public HarvestingDB(String dateid, String noofunits, String farmerid, String harvestkilos, String locale) {
        this.dateid = dateid;
        this.noofunits = noofunits;
        this.farmerid = farmerid;
        this.harvestkilos = harvestkilos;
        this.locale = locale;

    }


}