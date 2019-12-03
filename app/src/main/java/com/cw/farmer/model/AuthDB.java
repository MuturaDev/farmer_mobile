package com.cw.farmer.model;

import com.orm.SugarRecord;

public class AuthDB extends SugarRecord<AuthDB> {

    public String auth_key;

    public AuthDB() {
    }

    public AuthDB(String auth_key) {
        this.auth_key = auth_key;
    }


}