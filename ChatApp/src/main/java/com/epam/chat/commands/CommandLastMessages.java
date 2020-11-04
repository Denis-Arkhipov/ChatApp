package com.epam.chat.commands;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.db.SQLHendler;
import com.epam.chat.utilities.Utility;
import com.google.gson.Gson;

/**
 * Command for sending recent messages.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class CommandLastMessages implements Command {
    private static final int NUMBER_OF_MESSAGES = 50;
    private static final String MESSAGE_DAO = "messageDAO";
    private static final Logger logger = LoggerFactory.getLogger(
	    CommandLastMessages.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	MessageDAO messageDAO = (MessageDAO) req.getServletContext().
		getAttribute(MESSAGE_DAO);
	SQLHendler helper = new SQLHendler();
	String username = null;

	username = parseUsername(req);

	if (helper.userIsInTable(username)) {
	    Utility.sendResponse(resp, HttpServletResponse.SC_OK, 
		    messageDAO.getLast(NUMBER_OF_MESSAGES));
	} else {
	    Utility.sendResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, 
		    null);
	}
    }

    private String parseUsername(HttpServletRequest req) {
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
