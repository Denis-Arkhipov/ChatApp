package com.epam.chat.datalayer.dto;

/**
 * Represents chat user
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class User {

    private String username;
    private String password;
    private String email;
    private String photoName;
    private String phone;

    public User() {

    }

    public User(String username, String password, String email, 
	    String phone, String photoName) {
	this.username = username;
	this.password = password;
	this.email = email;
	this.phone = phone;
	this.photoName = photoName;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public String getUsername() {
	return username;
    }

    public void setUsername(String username) {
	this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
	return "User [username=" + username + ", password=" + password + 
		", email=" + email + ", photoName=" + photoName
		+ ", phone=" + phone + "]";
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((email == null) ? 0 : email.hashCode());
	result = prime * result + 
		((password == null) ? 0 : password.hashCode());
	result = prime * result + ((phone == null) ? 0 : phone.hashCode());
	result = prime * result + 
		((photoName == null) ? 0 : photoName.hashCode());
	result = prime * result + 
		((username == null) ? 0 : username.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	User other = (User) obj;
	if (email == null) {
	    if (other.email != null)
		return false;
	} else if (!email.equals(other.email))
	    return false;
	if (password == null) {
	    if (other.password != null)
		return false;
	} else if (!password.equals(other.password))
	    return false;
	if (phone == null) {
	    if (other.phone != null)
		return false;
	} else if (!phone.equals(other.phone))
	    return false;
	if (photoName == null) {
	    if (other.photoName != null)
		return false;
	} else if (!photoName.equals(other.photoName))
	    return false;
	if (username == null) {
	    if (other.username != null)
		return false;
	} else if (!username.equals(other.username))
	    return false;
	return true;
    }

}
