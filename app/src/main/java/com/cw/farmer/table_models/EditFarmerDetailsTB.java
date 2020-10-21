package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class EditFarmerDetailsTB extends SugarRecord<EditFarmerDetailsTB> {
    private String farmerId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String mobileNo;
    private String  gender;
    private String dateOfBirth;
    private String dateFormat;
    private String locale;


    public EditFarmerDetailsTB(String farmerId, String firstName, String middleName, String lastName, String mobileNo, String gender, String dateOfBirth, String dateFormat, String locale) {
        this.farmerId = farmerId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.mobileNo = mobileNo;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.dateFormat = dateFormat;
        this.locale = locale;
    }

    public EditFarmerDetailsTB() {
    }

    public String getFarmerId() {
        return farmerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getLocale() {
        return locale;
    }
}
