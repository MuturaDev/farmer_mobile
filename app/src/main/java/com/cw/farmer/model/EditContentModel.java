package com.cw.farmer.model;

import java.io.Serializable;
import java.util.ArrayList;

public class EditContentModel implements Serializable {

    private String title;
    private Object contentObject;
    private String errorMessage;
    private long tableID;
    private boolean status;

    public EditContentModel(String title, Object contentObject, String errorMessage, long tableID, boolean status) {
        this.title = title;
        this.contentObject = contentObject;
        this.errorMessage = errorMessage;
        this.tableID = tableID;
        this.status = status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public Object getContentObject() {
        return contentObject;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public long getTableID() {
        return tableID;
    }
}
