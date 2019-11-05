package com.cw.farmer.model;

import com.orm.SugarRecord;

public class BankNameDB extends SugarRecord<BankNameDB> {

    public int id_db;
    public String bankname_db;
    public String accountformat_db;

    public BankNameDB() {
    }

    public BankNameDB(int id_db, String bankname_db, String accountformat_db) {
        this.id_db = id_db;
        this.bankname_db = bankname_db;
        this.accountformat_db = accountformat_db;
    }


}