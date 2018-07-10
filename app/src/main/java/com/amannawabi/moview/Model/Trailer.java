/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.moview.Model;

public class Trailer {

    private final String sTrailerId;
    private final String sTrailerKey;
    private final String sType;
    private final String sName;


    public Trailer(String sTrailerId, String sTrailerKey, String sType, String sName) {
        this.sTrailerId = sTrailerId;
        this.sTrailerKey = sTrailerKey;
        this.sType = sType;
        this.sName = sName;
    }

    public String getsName() {
        return sName;
    }

    public String getsTrailerId() {
        return sTrailerId;
    }

    public String getsTrailerKey() {
        return sTrailerKey;
    }

    public String getsType() {
        return sType;
    }
}
