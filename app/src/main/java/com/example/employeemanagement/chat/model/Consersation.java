package com.example.employeemanagement.chat.model;

import java.util.ArrayList;



public class Consersation {
    private ArrayList<MessageModel> listMessageData;
    public Consersation(){
        listMessageData = new ArrayList<>();
    }

    public ArrayList<MessageModel> getListMessageData() {
        return listMessageData;
    }
}
