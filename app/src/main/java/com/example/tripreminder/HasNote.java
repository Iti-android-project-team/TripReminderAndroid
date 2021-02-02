package com.example.tripreminder;

import android.os.Parcel;

import java.io.Serializable;

public class HasNote implements Serializable {

    private String body;
    private boolean isChecked;


    public HasNote(String body) {
        this.body = body;
    }


    public HasNote() {
    }


    public HasNote(String body, boolean isChecked) {
        this.body = body;
        this.isChecked = isChecked;
    }


    protected HasNote(Parcel in) {
        body = in.readString();
        isChecked = in.readByte() != 0;
    }


    public boolean isChecked() {
        return isChecked;
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public String toString() {
        return "Note{" +
                "body='" + body + '\'' +
//                ", isChecked=" + isChecked +
                '}';
    }

}