package com.aniketvishal.commonindianwords.Models;

/**
 * Created by aniketvishal on 04/12/17.
 */

public class MyWordsListModel {

    public String dbWord,dbMeaning;

    public MyWordsListModel(String dbWord, String dbMeaning) {
        this.dbWord = dbWord;
        this.dbMeaning = dbMeaning;
    }

    public MyWordsListModel() {
    }

    public String getDbWord() {
        return dbWord;
    }

    public void setDbWord(String dbWord) {
        this.dbWord = dbWord;
    }

    public String getDbMeaning() {
        return dbMeaning;
    }

    public void setDbMeaning(String dbMeaning) {
        this.dbMeaning = dbMeaning;
    }
}
