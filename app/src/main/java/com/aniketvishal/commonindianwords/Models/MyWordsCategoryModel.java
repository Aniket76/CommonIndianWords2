package com.aniketvishal.commonindianwords.Models;

/**
 * Created by aniketvishal on 04/12/17.
 */

public class MyWordsCategoryModel {

    public String dbcategory;

    public MyWordsCategoryModel(String dbcategory) {
        this.dbcategory = dbcategory;
    }

    public MyWordsCategoryModel() {
    }

    public String getDbcategory() {
        return dbcategory;
    }
}
