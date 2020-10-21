package com.cw.farmer.table_models;

import com.orm.SugarRecord;

public class FarmerDocument extends SugarRecord<FarmerDocument> {

    String docShortDescription;
    String docNumber;
    String docID;//docNumber = docID
    String entryID;
    String imageBytes;

    public FarmerDocument() {
    }

    public FarmerDocument(String docShortDescription, String docNumber, String docID, String entryID, String imageBytes) {
        this.docShortDescription = docShortDescription;
        this.docNumber = docNumber;
        this.docID = docID;
        this.entryID = entryID;
        this.imageBytes = imageBytes;
    }


    public String getDocShortDescription() {
        return docShortDescription;
    }

    public String getDocNumber() {
        return docNumber;
    }

    public String getDocID() {
        return docID;
    }

    public String getEntryID() {
        return entryID;
    }

    public String getImageBytes() {
        return imageBytes;
    }
}
