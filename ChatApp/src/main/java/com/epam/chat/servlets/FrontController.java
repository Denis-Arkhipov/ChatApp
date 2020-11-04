package com.epam.chat.servlets;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.commands.Command;
import com.epam.chat.commands.CommandImage;
import com.epam.chat.commands.CommandLastMessages;
import com.epam.chat.commands.CommandLogin;
import com.epam.chat.commands.CommandLogout;
import com.epam.chat.commands.CommandRegistration;
import com.epam.chat.commands.CommandSendMessage;
import com.epam.chat.commands.CommandUsersOnline;
import com.epam.chat.datalayer.DAOFactory;
import com.epam.chat.datalayer.DBType;
import com.epam.chat.datalayer.MessageDAO;
import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.utilities.Utility;

/**
 * Handling incoming requests Front Controller.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
@MultipartConfig
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String USER_DAO = "userDAO";
    private static final String MESSAGE_DAO = "messageDAO";
    private static final String LOGIN = "/ChatApp/Controller/user/login";
    private static final String LOGOUT = "/ChatApp/Controller/user/logout";
    private static final String REGISTRATION = "/ChatApp/Controller/"
    	+ "user/registration";
    private static final String SEND_MESSAGE = "/ChatApp/Controller/message";
    private static final String MESSAGE_HISTORY = "/ChatApp/Controller/"
    	+ "message/history";
    private static final String USERS_ONLINE = "/ChatApp/Controller/"
    	+ "users/online";
    private static final String IMAGE = "/ChatApp/Controller/image";

    private static final Logger logger = LoggerFactory.getLogger(
	    FrontController.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
	    throws ServletException, IOException {
	String url = req.getRequestURI(); 

	try {
	    defineCommand(url).execute(req, resp);
	} catch (NullPointerException e) {
	    logger.error("Invalid command url type: {}", url, e);
	    Utility.sendResponse(resp, HttpServletResponse.SC_BAD_REQUEST, 
		    "Command undefined!");
	}
    }

    private Command defineCommand(String commandName) {
	logger.debug("Incoming command name: {}", commandName);
	Command command = null;

	switch (commandName) {
	case LOGIN:
	    command = new CommandLogin();
	    break;
	case LOGOUT:
	    command = new CommandLogout();
	    break;
	case REGISTRATION:
	    command = new CommandRegistration();
	    break;
	case SEND_MESSAGE:
	    command = new CommandSendMessage();
	    break;
	case MESSAGE_HISTORY:
	    command = new CommandLastMessages();
	    break;
	case USERS_ONLINE:
	    command = new CommandUsersOnline();
	    break;
	case IMAGE:
	    command = new CommandImage();
	    break;
	default:
	    break;
	}
	
	logger.debug("Defined command: {}", command);
	return command;
    }

    @Override
    public void init() throws ServletException {
	DAOFactory factory = DAOFactory.getInstance(DBType.ORACLE);
	MessageDAO messageDAO = factory.getMessageDAO();
	UserDAO userDAO = factory.getUserDAO();
	ServletContext context = getServletContext();
	context.setAttribute(MESSAGE_DAO, messageDAO);
	context.setAttribute(USER_DAO, userDAO);

	super.init();
    }
}
