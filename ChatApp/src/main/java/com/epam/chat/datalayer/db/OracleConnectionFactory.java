package com.epam.chat.datalayer.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public final class OracleConnectionFactory {
    
    private static final Logger logger = Logger.getLogger(
	    OracleConnectionFactory.class);
    private static final String JNDI_NAME = "java:comp/env/jdbc/MyDB";
    
    private OracleConnectionFactory() {
	throw new UnsupportedOperationException();
    }
    
    public static Connection getConnection() {
	Connection connection = null;

	try {
	    Locale.setDefault(Locale.ENGLISH);
    
	    Context initContext = new InitialContext();
	    DataSource ds = (DataSource) initContext.lookup(JNDI_NAME);
	    connection = ds.getConnection();
	    
	} catch (NamingException e) {
	    logger.error(e.getMessage());
	} catch (NullPointerException | SQLException e) {
	    logger.error(e.getMessage());
	}
	
	return connection;
    }

}
