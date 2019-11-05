package com.cw.farmer.model;

import com.orm.SugarRecord;

public class SprayPostDB extends SugarRecord<SprayPostDB> {

    public String verificationid;
    public String cordinates;
    public String location;
    public String noofunits;
    public String sprayconfirmed;
    public String programid;

    public SprayPostDB() {
    }

    public SprayPostDB(String verificationid, String cordinates, String location, String noofunits, String sprayconfirmed, String programid) {
        this.verificationid = verificationid;
        this.cordinates = cordinates;
        this.location = location;
        this.noofunits = noofunits;
        this.sprayconfirmed = sprayconfirmed;
        this.programid = programid;

    }


}