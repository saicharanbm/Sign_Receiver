package com.example.converter;

public class Message {
    int Image;
    String Message;
    String Time;

    public Message(int image, String message, String time) {
        Image = image;
        Message = message;
        Time = time;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
