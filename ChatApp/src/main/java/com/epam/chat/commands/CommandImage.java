package com.epam.chat.commands;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.utilities.Utility;
import com.google.gson.Gson;

/**
 * Command for sending user image to HTML page.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class CommandImage implements Command {
    private static final String CATALINA_BASE = "catalina.base";
    private static final String PATH_DELIMITER = "\\";
    private static final String CONTENT_TYPE_IMAGE = "image/jpeg";
    private static final String PAHT_FILE_FOTO = "\\webapps\\ChatApp\\images";
    private static final Logger logger = LoggerFactory.getLogger(
	    CommandImage.class);

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {
	String photoName = null;
	resp.setContentType(CONTENT_TYPE_IMAGE);
	photoName = parsePhotoName(req);	
	String tomcatFolder = System.getProperty(CATALINA_BASE);
	
	try (FileInputStream fin = new FileInputStream(tomcatFolder + 
		PAHT_FILE_FOTO + PATH_DELIMITER + photoName)) {    
	    byte[] bytes = new byte[fin.available()];
	    fin.read(bytes, 0, fin.available());
	    resp.setContentLength((int) bytes.length);

	    ServletOutputStream outStream = resp.getOutputStream();
	    outStream.write(bytes);
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	}
    }

    private String parsePhotoName(HttpServletRequest req) {
	Gson gson = new Gson();
	String photoName = null;
	String data = null;

	try {
	    data = Utility.readAll(req.getReader());
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	}

	photoName = gson.fromJson(data, String.class);
	logger.debug("Received data: {}", photoName);

	return photoName;
    }
}
