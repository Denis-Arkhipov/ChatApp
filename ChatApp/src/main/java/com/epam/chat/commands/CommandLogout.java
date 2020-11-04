package com.epam.chat.commands;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.utilities.Utility;
import com.google.gson.Gson;

/**
 * Command to log out the user from the chat.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class CommandLogout implements Command {
    private static final String STATUS_LOGOUT = "LOGOUT";
    private static final String TITLE_NAME_DATE = "DateLogout";
    private static final String USER_DAO = "userDAO";
    private static final Logger logger = LoggerFactory.getLogger(
	    CommandLogout.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	UserDAO userDAO = (UserDAO) req.getServletContext().getAttribute(
		USER_DAO);
	String username = null;

	username = parseObjLogout(req);
	long dateLogin = Long.parseLong(req.getHeader(TITLE_NAME_DATE));
	Message message = new Message(dateLogin, username, "The user has "
		+ "loggedout.", STATUS_LOGOUT);
	userDAO.logout(message);

	Utility.sendResponse(resp, HttpServletResponse.SC_OK, null);
    }

    private String parseObjLogout(HttpServletRequest req) {
	Gson gson = new Gson();
	String username = null;
	String data = null;

	try {
	    data = Utility.readAll(req.getReader());
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	}

	username = gson.fromJson(data, String.class);
	logger.debug("Received data: {}", username);

	return username;
    }
}
