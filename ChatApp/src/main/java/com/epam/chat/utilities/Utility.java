package com.epam.chat.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * Utility class with static methods.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public final class Utility {
    private static final Logger logger = LoggerFactory.getLogger(Utility.class);

    private Utility() {
	throw new UnsupportedOperationException();
    }

    public static String readAll(Reader rd) {
	StringBuilder sb = new StringBuilder();
	String line;

	try (BufferedReader br = (BufferedReader) rd) {
	    while ((line = br.readLine()) != null) {
		sb.append(line);
	    }
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	}

	logger.debug("Data read: {}", sb);
	
	return sb.toString();
    }

    public static <T> void sendResponse(HttpServletResponse resp, int status, 
	    T t) {
	resp.setContentType("application/json");
	resp.setCharacterEncoding("UTF-8");
	resp.setStatus(status);
	logger.debug("Received data: {}", status, t);

	try {
	    resp.getWriter().write(new Gson().toJson(t));
	} catch (IOException e) {
	    logger.error("Invalid data: {}", e.getMessage(), e);
	}
    }

    public static Integer parseNumber(String strNumb) {
	Integer result = null;
	StringBuilder secondStr = new StringBuilder();

	for (int i = 0; i < strNumb.length(); i++) {
	    if (Character.isDigit(strNumb.charAt(i))) {
		secondStr.append(strNumb.charAt(i));
	    }
	}
	
	if (!"".equals(secondStr.toString())) {
	    result = Integer.parseInt(secondStr.toString());
	}

	return result;
    }
}
