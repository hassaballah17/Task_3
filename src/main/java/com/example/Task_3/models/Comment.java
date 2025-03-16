package com.example.Task_3.models;

public class Comment {
    private String text;
    private String date;

    public Comment(){

    }

    public Comment(String text, String date){
        this.text=text;
        this.date=date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
