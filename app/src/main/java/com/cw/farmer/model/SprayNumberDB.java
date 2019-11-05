package com.cw.farmer.model;

import com.orm.SugarRecord;

public class SprayNumberDB extends SugarRecord<SprayNumberDB> {

    public int id_spray;
    public String spray_no;

    public SprayNumberDB() {
    }

    public SprayNumberDB(int id_spray, String spray_no) {
        this.id_spray = id_spray;
        this.spray_no = spray_no;
    }


}