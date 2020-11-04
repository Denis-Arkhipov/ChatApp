package com.epam.chat.datalayer.dto;

/**
 * Represents chat message
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */

public class Message {
    private long date;
    private String username;
    private String textMessage;
    private String status;

    public Message(long date, String username, String message, String status) {
	this.date = date;
	this.username = username;
	this.textMessage = message;
	this.status = status;
    }

    public String getNameUser() {
	return username;
    }

    public void setNameUser(String user) {
	this.username = user;
    }

    public String getMessage() {
	return textMessage;
    }

    public void setMessage(String message) {
	this.textMessage = message;
    }
    
    public long getDate() {
	return date;
    }

    public void setDate(long date) {
	this.date = date;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public String toString() {
	return date + "  " + username + ": " + textMessage + "\n";
    }

}
