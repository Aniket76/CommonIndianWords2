package com.aniketvishal.commonindianwords.Models;

/**
 * Created by aniketvishal on 26/11/17.
 */

public class WordListModel {

    public String mWord;
    public String mMeaning;

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public String isFav;
    public String dbTable;

    public WordListModel(String mWord, String mMeaning, String isFav, String dbTable) {
        this.mWord = mWord;
        this.mMeaning = mMeaning;
        this.isFav = isFav;
        this.dbTable = dbTable;
    }

    public String getDbTable() {
        return dbTable;
    }

    public String getIsFav() {
        return isFav;
    }

    public String getmWord() {
        return mWord;
    }

    public String getmMeaning() {
        return mMeaning;
    }


}
