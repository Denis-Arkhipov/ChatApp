package com.epam.chat.datalayer;

import com.epam.chat.datalayer.db.OracleDAOFactory;

/**
 * Type of db
 */
public enum DBType {

    ORACLE {
        @Override
        public DAOFactory getDAOFactory() {
            return new OracleDAOFactory();
        }
    };

    /**
     * Get db type by name
     * @param dbType type founded by name
     * @return database type
     */
    public static DBType getTypeByName(String dbType) {
        try {
            return DBType.valueOf(dbType.toUpperCase());
        } catch (Exception e) {
            throw new DBTypeException();
        }
    }


    public abstract DAOFactory getDAOFactory();

}
