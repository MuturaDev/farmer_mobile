package com.cw.farmer.offlinefunctions;
public class OfflineDataItem {

    private Object DataItemObject;
    private int DataItemProgress;
    private int DataItemSize;
    private boolean DataItemCompleteStatus;
    private String DateItemTitle;
    private String DateItemErrorText;

    public OfflineDataItem(Object dataItemObject, int dataItemProgress, int dataItemSize, boolean dataItemCompleteStatus, String dateItemTitle) {
        DataItemObject = dataItemObject;
        DataItemProgress = dataItemProgress;
        DataItemSize = dataItemSize;
        DataItemCompleteStatus = dataItemCompleteStatus;
        DateItemTitle = dateItemTitle;
    }

    public String getDateItemErrorText() {
        return DateItemErrorText;
    }

    public void setDateItemErrorText(String dateItemErrorText) {
        DateItemErrorText = dateItemErrorText;
    }

    public Object getDataItemObject() {
        return DataItemObject;
    }

    public int getDataItemProgress() {
        return DataItemProgress;
    }

    public int getDataItemSize() {
        return DataItemSize;
    }

    public boolean isDataItemCompleteStatus() {
        return DataItemCompleteStatus;
    }

    public String getDateItemTitle() {
        return DateItemTitle;
    }


    public void setDataItemProgress(int dataItemProgress) {
        DataItemProgress = dataItemProgress;
    }

    public void setDataItemCompleteStatus(boolean dataItemCompleteStatus) {
        DataItemCompleteStatus = dataItemCompleteStatus;
    }

    @Override
    public String toString() {
        return "OfflineDataItem{" +
                "DataItemObject=" + DataItemObject +
                ", DataItemProgress=" + DataItemProgress +
                ", DataItemSize=" + DataItemSize +
                ", DataItemCompleteStatus=" + DataItemCompleteStatus +
                ", DateItemTitle='" + DateItemTitle + '\'' +
                '}';
    }
}
