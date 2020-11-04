package com.epam.chat.commands;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.datalayer.db.SQLHendler;
import com.epam.chat.datalayer.dto.User;
import com.epam.chat.utilities.Utility;

/**
 * The command for registering a user in the chat. Sends user data to the
 * database.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class CommandRegistration implements Command {
    private static final String CATALINA_BASE = "catalina.base";
    private static final String PATH_DELIMITER = "\\";
    private static final String EMPTY_LINE = "";
    private static final String PARAMETER_NAME_PHONE = "phone";
    private static final String PARAMETER_NAME_EMAIL = "Email";
    private static final String PARAMETER_NAME_PASS_REG = "passwordReg";
    private static final String PARAMETER_NAME_LOGIN_REG = "loginReg";
    private static final String PART_NAME_PHOTO = "photo";
    private static final String PAHT_FILE_FOTO = "\\webapps\\ChatApp\\images";
    private static final Logger logger = LoggerFactory.getLogger(CommandRegistration.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	SQLHendler helper = new SQLHendler();
	User user = null;
	Part filePart = null;

	try {
	    filePart = req.getPart(PART_NAME_PHOTO);
	    user = parseRegForm(req, filePart);
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	} catch (ServletException e) {
	    logger.error("Server error: {}", e.getMessage(), e);
	}

	if (!user.getUsername().equals(EMPTY_LINE) && !user.getEmail().equals(EMPTY_LINE)
		&& !user.getPassword().equals(EMPTY_LINE) && !helper.userIsInTable(user.getUsername())) {
	    String tomcatFolder = System.getProperty(CATALINA_BASE);
	    Path folder = Paths.get(tomcatFolder, PAHT_FILE_FOTO, PATH_DELIMITER, user.getPhotoName());
	    logger.debug("File path: {}", folder);

	    try (InputStream is = filePart.getInputStream()) {
		if (!Files.exists(folder.getParent())) {
		    Files.createDirectories(folder.getParent());		    
		} 
		
		Files.copy(is, folder);
	    } catch (IOException e) {
		logger.error("Invalid data: {}", e.getMessage(), e);
	    }

	    helper.addUserDB(user);
	    Utility.sendResponse(resp, HttpServletResponse.SC_OK, null);
	} else {
	    Utility.sendResponse(resp, HttpServletResponse.SC_UNAUTHORIZED, null);
	}
    }

    private User parseRegForm(HttpServletRequest req, Part file) {
	String username = req.getParameter(PARAMETER_NAME_LOGIN_REG);
	String password = req.getParameter(PARAMETER_NAME_PASS_REG);
	String email = req.getParameter(PARAMETER_NAME_EMAIL);
	String phone = req.getParameter(PARAMETER_NAME_PHONE);
	String photoName = file.getSubmittedFileName();
	User user = new User(username, password, email, phone, photoName);

	logger.debug("Defined data: {}", user);
	return user;
    }
}
