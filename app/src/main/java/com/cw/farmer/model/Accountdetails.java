package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Accountdetails {

    @SerializedName("accountno")
    @Expose
    private String accountno;
    @SerializedName("bankId")
    @Expose
    private int bankId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("filetype")
    @Expose
    private String filetype;

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public Accountdetails withAccountno(String accountno) {
        this.accountno = accountno;
        return this;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public Accountdetails withBankId(int bankId) {
        this.bankId = bankId;
        return this;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Accountdetails withImage(String image) {
        this.image = image;
        return this;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Accountdetails withFiletype(String filetype) {
        this.filetype = filetype;
        return this;
    }

}