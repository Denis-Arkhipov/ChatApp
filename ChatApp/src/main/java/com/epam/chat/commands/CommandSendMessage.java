package com.epam.chat.commands;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.utilities.Utility;
import com.google.gson.Gson;

/**
 * Command to send a message. 
 * Saves the message to the database.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class CommandSendMessage implements Command {
    private static final String EMPTY_LINE = "";
    private static final int ONE = 1;
    private static final String MESSAGE_DAO = "messageDAO";
    private static final Logger logger = LoggerFactory.getLogger(
	    CommandSendMessage.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	MessageDAO messageDAO = (MessageDAO) req.getServletContext().
		getAttribute(MESSAGE_DAO);
	Message message = null;

	message = parseMessage(req);

	if (message != null && !message.getMessage().equals(EMPTY_LINE)) {
	    messageDAO.sendMessage(message);
	    List<Message> lastMessage = messageDAO.getLast(ONE);

	    Utility.sendResponse(resp, HttpServletResponse.SC_OK, lastMessage);
	} else {
	    Utility.sendResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, 
		    null);
	}

    }

    private Message parseMessage(HttpServletRequest req) {
	Gson gson = new Gson();
	Message message = null;
	String data = null;

	try {
	    data = Utility.readAll(req.getReader());
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	}

	message = gson.fromJson(data, Message.class);
	logger.debug("Received data: {}", message);

	return message;
    }
}
