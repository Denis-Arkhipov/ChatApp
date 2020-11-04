package com.epam.chat.commands;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.db.SQLHendler;
import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.utilities.Utility;
import com.google.gson.Gson;

/**
 * Command for processing user data to enter the chat.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class CommandLogin implements Command {
    private static final String STATUS_LOGIN = "LOGIN";
    private static final String MESSAGE_DAO = "messageDAO";
    private static final String USER_DAO = "userDAO";
    private static final String TITLE_NAME_LOGIN_DATE = "LoginDate";
    private static final Logger logger = LoggerFactory.getLogger(
	    CommandLogin.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	UserDAO userDAO = (UserDAO) req.getServletContext().getAttribute(
		USER_DAO);
	MessageDAO messageDAO = (MessageDAO) req.getServletContext().
		getAttribute(MESSAGE_DAO);
	SQLHendler helper = new SQLHendler();
	User user = null;

	user = parseLoginForm(req);

	if (userDAO.isLogin(user)) {
	    long dateLogin = Long.parseLong(req.getHeader(
		    TITLE_NAME_LOGIN_DATE));
	    Message message = new Message(dateLogin, user.getUsername(), "The "
	    	+ "user has entered the chat.", STATUS_LOGIN);
	    messageDAO.sendMessage(message);
	    user = helper.getUserData(user.getUsername());

	    Utility.sendResponse(resp, HttpServletResponse.SC_OK, user);
	} else {
	    Utility.sendResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, 
		    null);
	}
    }

    private User parseLoginForm(HttpServletRequest req) {
	Gson gson = new Gson();
	User user = null;
	String data = null;

	try {
	    data = Utility.readAll(req.getReader());
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	}

	user = gson.fromJson(data, User.class);
	logger.debug("Received data: {}", user);

	return user;
    }
}
