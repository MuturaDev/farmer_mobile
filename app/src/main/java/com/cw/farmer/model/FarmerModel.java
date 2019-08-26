package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerModel {

    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("idno")
    @Expose
    private String idno;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("activated")
    @Expose
    private String activated;
    @SerializedName("centerid")
    @Expose
    private int centerid;
    @SerializedName("dateFormat")
    @Expose
    private String dateFormat;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("accountdetails")
    @Expose
    private Accountdetails accountdetails;
    @SerializedName("identitydetails")
    @Expose
    private Identitydetails identitydetails;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public FarmerModel withFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public FarmerModel withMobileno(String mobileno) {
        this.mobileno = mobileno;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public FarmerModel withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public FarmerModel withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getIdno(String trim) {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public FarmerModel withIdno(String idno) {
        this.idno = idno;
        return this;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public FarmerModel withDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public String getActivated() {
        return activated;
    }

    public void setActivated(String activated) {
        this.activated = activated;
    }

    public FarmerModel withActivated(String activated) {
        this.activated = activated;
        return this;
    }

    public int getCenterid() {
        return centerid;
    }

    public void setCenterid(int centerid) {
        this.centerid = centerid;
    }

    public FarmerModel withCenterid(int centerid) {
        this.centerid = centerid;
        return this;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public FarmerModel withDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public FarmerModel withLocale(String locale) {
        this.locale = locale;
        return this;
    }

    public Accountdetails getAccountdetails() {
        return accountdetails;
    }

    public void setAccountdetails(Accountdetails accountdetails) {
        this.accountdetails = accountdetails;
    }

    public FarmerModel withAccountdetails(Accountdetails accountdetails) {
        this.accountdetails = accountdetails;
        return this;
    }

    public Identitydetails getIdentitydetails() {
        return identitydetails;
    }

    public void setIdentitydetails(Identitydetails identitydetails) {
        this.identitydetails = identitydetails;
    }

    public FarmerModel withIdentitydetails(Identitydetails identitydetails) {
        this.identitydetails = identitydetails;
        return this;
    }

}