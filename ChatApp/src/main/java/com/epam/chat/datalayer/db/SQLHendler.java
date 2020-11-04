package com.epam.chat.datalayer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.chat.datalayer.dto.Message;
import com.epam.chat.datalayer.dto.User;

/**
 * A class that implements methods for working with a database.
 * 
 * @author Arkhipov Denis
 * @version 1.0 2020
 */

public class SQLHendler {
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final String SELECT_GET_LAST_MESSAGES = 
	    "selectGetLastMessages";
    private static final String INSERT_CREATE_MESSAGE = "insertCreateMessage";
    private static final String INSERT_CREATE_USER = "insertUser";
    private static final String SELECT_USER_DATA = "selectUserData";
    private static final String SELECT_LOGGED_USERS = "selectGetAllLoggedUsers";

    private static final Logger logger = LoggerFactory.getLogger(
	    SQLHendler.class);
    private ResourceBundle bundle = ResourceBundle.getBundle("query");

    /* Methods UserDAO */
    /**
     * Compares user data with data in the database
     * 
     * @param user
     * @return
     */
    public boolean userLogin(User user) {
	logger.debug("Incoming data: {}", user);
	String query = bundle.getString(SELECT_USER_DATA);
	boolean result = false;

	try (Connection connection = OracleConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			query)) {
	    statement.setString(ONE, user.getUsername());

	    try (ResultSet resultSet = statement.executeQuery()) {
		if (resultSet.next()) {
		    String username = resultSet.getString(ONE);
		    String password = resultSet.getString(TWO);
		    String email = resultSet.getString(THREE);
		    logger.debug("Received data: {}", username, password,
			    email);

		    if ((username.equals(user.getUsername()) && 
			    password.equals(user.getPassword()))
			    || (email.equals(user.getEmail()) && 
				    password.equals(user.getPassword()))) {
			result = true;
		    }
		}
	    }
	} catch (SQLException e) {
	    logger.error("Database connection error: {}", e.getMessage(), e);
	}

	logger.debug("Defined data: {}", result);
	return result;
    }

    /**
     * Adds a message to the database stating that the user has left the chat
     * 
     * @param user
     */
    public void userExit(Message userToLogout) {
	logger.debug("Incoming data: {}", userToLogout);
	addMessageDB(userToLogout);
    }

    /**
     * Adds a new user to the database.
     * 
     * @param user
     */
    public void addUserDB(User user) {
	logger.debug("Incoming data: {}", user);
	String query = bundle.getString(INSERT_CREATE_USER);
	String username = user.getUsername();
	String password = user.getPassword();
	String email = user.getEmail();
	String phone = user.getPhone();
	String photoName = user.getPhotoName();

	try (Connection connection = OracleConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			query)) {
	    statement.setString(ONE, username);
	    statement.setString(TWO, password);
	    statement.setString(THREE, email);
	    statement.setString(FOUR, phone);
	    statement.setString(FIVE, photoName);
	    statement.executeUpdate();
	} catch (SQLException e) {
	    logger.error("Database connection error: {}", e.getMessage(), e);
	}
    }

    public boolean userIsInTable(String username) {
	logger.debug("Incoming data: {}", username);
	String query = bundle.getString(SELECT_USER_DATA);
	boolean result = false;

	try (Connection connection = OracleConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			query)) {
	    statement.setString(ONE, username);

	    try (ResultSet resultSet = statement.executeQuery()) {
		if (resultSet.next()) {
		    result = true;
		}
	    }

	} catch (SQLException e) {
	    logger.error("Database connection error: {}", e.getMessage(), e);
	}

	logger.debug("Defined data: {}", result);
	return result;
    }

/**
 * Get all logged in users
 * 
 * @return Online user list
 */
    public List<User> getAllLoggedUsers() {
	List<User> listUsers = new ArrayList<>();
	String query = bundle.getString(SELECT_LOGGED_USERS);

	try (Connection connection = OracleConnectionFactory.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery(query)) {
	    while (resultSet.next()) {
		User user = new User();
		user.setUsername(resultSet.getString(ONE));
		listUsers.add(user);
	    }
	} catch (SQLException e) {
	    logger.error("Database connection error: {}", e.getMessage(), e);
	}

	logger.debug("Defined data: {}", listUsers);
	return listUsers;
    }

    /**
     * Get the latest n posts.
     * 
     * @param count Number of messages
     * @return
     */
    public User getUserData(String username) {
	logger.debug("Incoming data: {}", username);
	User user = new User();
	String query = bundle.getString(SELECT_USER_DATA);

	try (Connection connection = OracleConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			query)) {
	    statement.setString(ONE, username);

	    try (ResultSet result = statement.executeQuery()) {
		while (result.next()) {
		    user.setUsername(result.getString(ONE));
		    user.setPassword(result.getString(TWO));
		    user.setEmail(result.getString(THREE));
		    user.setPhone(result.getString(FOUR));
		    user.setPhotoName(result.getString(FIVE));
		}
	    }
	    
	} catch (SQLException e) {
	    logger.error("Database connection error: {}", e.getMessage(), e);
	}

	logger.debug("Defined data: {}", user);
	return user;
    }

    /* Methods MessageDAO */
    /**
     * Adds a message to the database.
     * 
     * @param message
     */
    public void addMessageDB(Message message) {
	logger.debug("Incoming data: {}", message);
	String query = bundle.getString(INSERT_CREATE_MESSAGE);
	String nameUser = message.getNameUser();
	String textMessage = message.getMessage();
	long date = message.getDate();
	int status = getNumStatus(message.getStatus());

	try (Connection connection = OracleConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			query)) {
	    statement.setString(ONE, nameUser);
	    statement.setString(TWO, textMessage);
	    statement.setLong(THREE, date);
	    statement.setInt(FOUR, status);

	    statement.executeUpdate();

	} catch (SQLException e) {
	    logger.error("Database connection error: {}", e.getMessage(), e);
	}
    }

    /**
     * Get the latest n posts.
     * 
     * @param count Number of messages
     * @return
     */
    public List<Message> getLastMessages(int count) {
	logger.debug("Incoming data: {}", count);
	String query = bundle.getString(SELECT_GET_LAST_MESSAGES);
	List<Message> listMessage = new ArrayList<>();
	String userName = null;
	String message = null;
	String status = null;
	long date = 0L;

	try (Connection connection = OracleConnectionFactory.getConnection();
		PreparedStatement statement = connection.prepareStatement(
			query)) {
	    statement.setInt(ONE, count);

	    try (ResultSet result = statement.executeQuery()) {
		while (result.next()) {
		    userName = result.getString(ONE);
		    message = result.getString(TWO);
		    date = result.getLong(THREE);
		    status = result.getString(FOUR);

		    listMessage.add(new Message(date, userName, message,
			    status));
		}
	    }

	} catch (SQLException e) {
	    logger.error("Database connection error: {}", e.getMessage(), e);
	}

	logger.debug("Defined data: {}", listMessage);
	return listMessage;
    }

    /**
     * Associates Status with number
     * 
     * @param status
     * @return numeric representation of status
     */
    public int getNumStatus(String status) {
	logger.debug("Incoming data: {}", status);
	int num = 0;

	switch (status) {
	case "LOGIN":
	    num = ONE;
	    break;
	case "LOGOUT":
	    num = TWO;
	    break;
	case "KICK":
	    num = THREE;
	    break;
	case "MESSAGE":
	    num = FOUR;
	    break;
	default:
	    num = TWO;
	    break;
	}

	logger.debug("Defined data: {}", num);
	return num;
    }

}
