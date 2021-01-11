package com.example.app2;

import android.graphics.Color;

import java.util.Date;
import java.util.Random;

public class Message {
    public String message;
    public String sender;
    public Date createdAt;

    public void Message(String msg, String sender, Date createdat) {
        this.createdAt = createdat;
        this.sender = sender;
        this.message = msg;
    }
}
