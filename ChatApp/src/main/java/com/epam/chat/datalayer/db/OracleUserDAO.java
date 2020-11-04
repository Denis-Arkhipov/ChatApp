package com.epam.chat.datalayer.db;

import com.epam.chat.datalayer.UserDAO;
import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.datalayer.dto.User;

public class OracleUserDAO implements UserDAO {

    private SQLHendler helper = new SQLHendler();

    @Override
    public boolean isLogin(User userToLogin) {
	return helper.userLogin(userToLogin);
    }

    @Override
    public void logout(Message userToLogout) {
	helper.userExit(userToLogout);
    }

}
