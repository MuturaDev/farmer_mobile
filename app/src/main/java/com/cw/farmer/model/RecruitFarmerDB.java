package com.cw.farmer.model;

import com.orm.SugarRecord;

public class RecruitFarmerDB extends SugarRecord<RecruitFarmerDB> {

    public String farmerid;
    public String dateid;
    public String landownership;
    public String cordinates;
    public String location;
    public String noofunits;
    public String section;

    public RecruitFarmerDB() {
    }

    public RecruitFarmerDB(String farmerid, String dateid, String landownership, String cordinates, String location, String noofunits, String section) {
        this.farmerid = farmerid;
        this.dateid = dateid;
        this.landownership = landownership;
        this.cordinates = cordinates;
        this.location = location;
        this.noofunits = noofunits;
        this.section = section;
    }


}