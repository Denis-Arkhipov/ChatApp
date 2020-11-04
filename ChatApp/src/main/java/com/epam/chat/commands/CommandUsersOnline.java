package com.epam.chat.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.chat.datalayer.db.SQLHendler;
import com.epam.chat.utilities.Utility;

/**
 * Command to send a list of all online users.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */
public class CommandUsersOnline implements Command {
    
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp) {	
	Utility.sendResponse(resp, HttpServletResponse.SC_OK, new SQLHendler().
		getAllLoggedUsers());
    }
}
