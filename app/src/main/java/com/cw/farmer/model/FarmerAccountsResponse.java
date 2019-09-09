package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmerAccountsResponse {

    @SerializedName("base64ImageData")
    @Expose
    private String base64ImageData;
    @SerializedName("accountno")
    @Expose
    private String accountno;
    @SerializedName("bankName")
    @Expose
    private String bankName;
    @SerializedName("id")
    @Expose
    private int id;

    public String getBase64ImageData() {
        return base64ImageData;
    }

    public void setBase64ImageData(String base64ImageData) {
        this.base64ImageData = base64ImageData;
    }

    public FarmerAccountsResponse withBase64ImageData(String base64ImageData) {
        this.base64ImageData = base64ImageData;
        return this;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public FarmerAccountsResponse withAccountno(String accountno) {
        this.accountno = accountno;
        return this;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public FarmerAccountsResponse withBankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FarmerAccountsResponse withId(int id) {
        this.id = id;
        return this;
    }

}