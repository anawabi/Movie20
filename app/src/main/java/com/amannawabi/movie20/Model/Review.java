/*
 * Copyright (C) 2013 The Android Open Source Project
 */

package com.amannawabi.movie20.Model;

public class Review {

    private final String sAuthor;
    private final String sContent;

    public Review(String sAuthor, String sContent) {
        this.sAuthor = sAuthor;
        this.sContent = sContent;
    }

    public String getsAuthor() {
        return sAuthor;
    }

    public String getsContent() {
        return sContent;
    }
}
