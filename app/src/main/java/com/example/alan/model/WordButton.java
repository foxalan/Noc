package com.example.alan.model;

import android.widget.Button;

/**
 * Created by alan on 2017/3/20.
 */

public class WordButton {

    private boolean isvisable;
    private String word;
    private int id;
    private Button mViewButton;

    public WordButton(){
    }

    public WordButton(boolean isvisable, String word, int id, Button mViewButton) {
        this.isvisable = isvisable;
        this.word = word;
        this.id = id;
        this.mViewButton = mViewButton;
    }

    public boolean isIsvisable() {
        return isvisable;
    }

    public void setIsvisable(boolean isvisable) {
        this.isvisable = isvisable;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Button getmViewButton() {
        return mViewButton;
    }

    public void setmViewButton(Button mViewButton) {
        this.mViewButton = mViewButton;
    }
}
