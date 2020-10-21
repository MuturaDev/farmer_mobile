package com.cw.farmer.crashreporting.error;

import java.io.Serializable;

/**
 * Created by alhazmy13 on 2/13/17.
 */

public class Firmware implements Serializable {
    private String sdk;
    private String release;
    private String incremental;

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public String getRelease() {
        return release;
    }

    public  void setRelease(String release) {
        this.release = release;
    }

    public String getIncremental() {
        return incremental;
    }

    public  void setIncremental(String incremental) {
        this.incremental = incremental;
    }

    @Override
    public String toString() {
        return "Firmware{\n" +
                "'sdk'='" + sdk + "'\n"  +
                ", 'release'='" + release + "'\n"  +
                ", 'incremental'='" + incremental + "'\n"  +
                '}';
    }
}