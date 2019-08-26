package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class ErrorMsg {


    @SerializedName("developerMessage")
    @Expose
    private String developerMessage;
    @SerializedName("defaultUserMessage")
    @Expose
    private String defaultUserMessage;
    @SerializedName("userMessageGlobalisationCode")
    @Expose
    private String userMessageGlobalisationCode;
    @SerializedName("parameterName")
    @Expose
    private String parameterName;
    @SerializedName("value")
    @Expose
    private Object value;
//    @SerializedName("args")
//    @Expose
//    private List<Arg> args = null;

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getDefaultUserMessage() {
        return defaultUserMessage;
    }

    public void setDefaultUserMessage(String defaultUserMessage) {
        this.defaultUserMessage = defaultUserMessage;
    }

    public String getUserMessageGlobalisationCode() {
        return userMessageGlobalisationCode;
    }

    public void setUserMessageGlobalisationCode(String userMessageGlobalisationCode) {
        this.userMessageGlobalisationCode = userMessageGlobalisationCode;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

//    public List<Arg> getArgs() {
//        return args;
//    }
//
//    public void setArgs(List<Arg> args) {
//        this.args = args;
//    }
}
