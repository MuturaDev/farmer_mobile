package com.cw.farmer.model;

import com.orm.SugarRecord;

public class FarmerModelDB extends SugarRecord<FarmerModelDB> {


    public String firstname;
    public String mobileno;
    public String email;
    public String gender;
    public String idno;
    public String dateOfBirth;
    public String activated;
    public int centerid;
    public String dateFormat;
    public String locale;
    public String accountno;
    public int bankId;
    public String image_bank;
    public String filetype_bank;
    public int docId;
    public String image_id;
    public String filetype_id;
    public String docno_id;

    public FarmerModelDB() {
    }

    public FarmerModelDB(String firstname, String mobileno, String email, String gender, String idno, String dateOfBirth, String activated, int centerid, String dateFormat, String locale, String accountno, int bankId, String image_bank, String filetype_bank, int docId, String image_id, String filetype_id, String docno_id) {
        this.firstname = firstname;
        this.mobileno = mobileno;
        this.email = email;
        this.gender = gender;
        this.idno = idno;
        this.dateOfBirth = dateOfBirth;
        this.activated = activated;
        this.centerid = centerid;
        this.dateFormat = dateFormat;
        this.locale = locale;
        this.accountno = accountno;
        this.bankId = bankId;
        this.image_bank = image_bank;
        this.filetype_bank = filetype_bank;
        this.docId = docId;
        this.image_id = image_id;
        this.filetype_id = filetype_id;
        this.docno_id = docno_id;
    }

}