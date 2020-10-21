package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class FarmerAccountTB extends SugarRecord<FarmerAccountTB> {

    private String bankName;
    private String accountNo;
    private String accountName;
    private String entryID;
    private String ImageBytes;

    public FarmerAccountTB() {
    }

    public FarmerAccountTB(String bankName, String accountNo, String accountName, String entryID, String imageBytes) {
        this.bankName = bankName;
        this.accountNo = accountNo;
        this.accountName = accountName;
        this.entryID = entryID;
        ImageBytes = imageBytes;
    }


    public String getBankName() {
        return bankName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getEntryID() {
        return entryID;
    }

    public String getImageBytes() {
        return ImageBytes;
    }
}
