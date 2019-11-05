package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankNameResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("bankid")
    @Expose
    private String bankid;
    @SerializedName("bankname")
    @Expose
    private String bankname;
    @SerializedName("accountformat")
    @Expose
    private String accountformat;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BankNameResponse withId(int id) {
        this.id = id;
        return this;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public BankNameResponse withBankid(String bankid) {
        this.bankid = bankid;
        return this;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public BankNameResponse withBankname(String bankname) {
        this.bankname = bankname;
        return this;
    }

    public String getAccountformat() {
        return accountformat;
    }

    public void setAccountformat(String accountformat) {
        this.accountformat = accountformat;
    }

    public BankNameResponse withAccountformat(String accountformat) {
        this.accountformat = accountformat;
        return this;
    }


}