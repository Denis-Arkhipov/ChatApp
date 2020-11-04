package com.epam.chat.datalayer;

import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.datalayer.dto.User;

/**
 * Describes access interface to user Data Access Object
 */
public interface UserDAO {

    /**
     * Login user
     * 
     * @param userToLogin user we want to login
     */
    boolean isLogin(User userToLogin);

    /**
     * Logout user from system
     * 
     * @param userToLogout user we want to logout
     */
    void logout(Message userToLogout);

}
