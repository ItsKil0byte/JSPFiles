package com.example.database;

import com.example.models.UserProfile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBService {
    private final Connection connection;
    private static final Logger logger = Logger.getLogger(DBService.class.getName());

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public DBService() throws SQLException {
        this.connection = getMySQLConnection();
        logger.info("Successful connected to database!");
    }

    public void init() {
        try {
            new UsersDAO(connection).createUserTable();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clean() {
        UsersDAO usersDAO = new UsersDAO(connection);
        try {
            usersDAO.dropTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getMySQLConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/jsp_files",
                    "root",
                    "14022024"
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printConnectionInfo() {
        try {
            logger.info("Database Name: " + connection.getMetaData().getDatabaseProductName());
            logger.info("Database Version: " + connection.getMetaData().getDatabaseProductVersion());
            logger.info("Database Driver: " + connection.getMetaData().getDriverName());
            logger.info("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int addNewUser(String login, String password) {
        try {
            UsersDAO usersDAO = new UsersDAO(connection);
            usersDAO.insertUser(login, password);
            return usersDAO.getUserId(login);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UserProfile getUser(String login) {
        try {
            return new UsersDAO(connection).get(login);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
