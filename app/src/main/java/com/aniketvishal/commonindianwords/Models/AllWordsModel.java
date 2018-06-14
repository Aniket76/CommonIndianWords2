package com.aniketvishal.commonindianwords.Models;

/**
 * Created by aniketvishal on 26/11/17.
 */

public class AllWordsModel  {

    public String mWordCategory;
    private int mImageRes;

    public AllWordsModel(String mWordCategory, int mImageRes) {
        this.mWordCategory = mWordCategory;
        this.mImageRes = mImageRes;
    }

    public int getmImageRes() {
        return mImageRes;
    }

    public void setmImageRes(int mImageRes) {
        this.mImageRes = mImageRes;
    }

    public String getmWordCategory() {
        return mWordCategory;
    }

    public void setmWordCategory(String mWordCategory) {
        this.mWordCategory = mWordCategory;
    }
}
