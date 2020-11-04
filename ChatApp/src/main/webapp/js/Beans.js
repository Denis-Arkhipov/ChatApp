/**
 * Represents a message and chat user
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */ 
function User(username, password, email, phone, photo) {
	this.username = username;
	this.password = password;
	this.email = email;
	this.phone = phone;
	this.photo = photo;
}

function Message(date, username, textMessage, status) {
	this.date = date;
	this.username = username;
	this.textMessage = textMessage;
	this.status = status;
}