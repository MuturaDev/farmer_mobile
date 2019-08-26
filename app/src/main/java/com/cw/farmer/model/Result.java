package com.cw.farmer.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Result {


    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userId")
    @Expose
    private Integer userId;
    @SerializedName("base64EncodedAuthenticationKey")
    @Expose
    private String base64EncodedAuthenticationKey;
    @SerializedName("authenticated")
    @Expose
    private Boolean authenticated=false;
    @SerializedName("roles")
    @Expose
    private ArrayList<Role> roles = null;
    @SerializedName("permissions")
    @Expose
    private List<String> permissions = null;
    @SerializedName("shouldRenewPassword")
    @Expose
    private Boolean shouldRenewPassword;
    @SerializedName("isTwoFactorAuthenticationRequired")
    @Expose
    private Boolean isTwoFactorAuthenticationRequired;


    @SerializedName("developerMessage")
    @Expose
    private String developerMessage;
    @SerializedName("httpStatusCode")
    @Expose
    private String httpStatusCode;
    @SerializedName("defaultUserMessage")
    @Expose
    private String defaultUserMessage;
    @SerializedName("userMessageGlobalisationCode")
    @Expose
    private String userMessageGlobalisationCode;
    @SerializedName("errors")
    @Expose
    private List<Object> errors = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBase64EncodedAuthenticationKey() {
        return base64EncodedAuthenticationKey;
    }

    public void setBase64EncodedAuthenticationKey(String base64EncodedAuthenticationKey) {
        this.base64EncodedAuthenticationKey = base64EncodedAuthenticationKey;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public Boolean getShouldRenewPassword() {
        return shouldRenewPassword;
    }

    public void setShouldRenewPassword(Boolean shouldRenewPassword) {
        this.shouldRenewPassword = shouldRenewPassword;
    }

    public Boolean getIsTwoFactorAuthenticationRequired() {
        return isTwoFactorAuthenticationRequired;
    }

    public void setIsTwoFactorAuthenticationRequired(Boolean isTwoFactorAuthenticationRequired) {
        this.isTwoFactorAuthenticationRequired = isTwoFactorAuthenticationRequired;
    }


    public String getDeveloperMessage() {
        return developerMessage;
    }

    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    public String getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(String httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
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

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }
}
