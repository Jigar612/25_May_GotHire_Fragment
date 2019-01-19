package com.jigar.android.gothire;

/**
 * Created by COMP11 on 23-Jun-18.
 */

public class RecyclerModel {
    private String tagText;

    public RecyclerModel(String tagText){
        this.tagText = tagText;
    }
    public String getTagText() {
        return tagText;
    }

    public void setTagText(String tagText) {
        this.tagText = tagText;
    }
}