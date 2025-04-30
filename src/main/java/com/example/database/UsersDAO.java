package com.example.database;

import com.example.executor.Executor;
import com.example.models.UserProfile;

import java.sql.Connection;
import java.sql.SQLException;

public class UsersDAO {
    private final Executor executor;

    public UsersDAO(Connection connection) {
        this.executor = new Executor(connection);
    }

    public void createUserTable() throws SQLException {
        executor.execUpdate("CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "login VARCHAR(50) UNIQUE NOT NULL," +
                            "password VARCHAR(255) NOT NULL)"
        );
    }

    public void dropTable() throws SQLException {
        executor.execUpdate("DROP TABLE users");
    }

    public void insertUser(String login, String password) throws SQLException {
        executor.execUpdate("INSERT INTO users (login, password) VALUES (?, ?)", login, password);
    }

    public int getUserId(String login) throws SQLException {
        return executor.execQuery("SELECT * FROM users WHERE login = ?", resultSet -> {
            if (!resultSet.next()) {
                return -1;
            }
            return resultSet.getInt("id");
        }, login);
    }

    public UserProfile get(String login) throws SQLException {
        return executor.execQuery("SELECT * FROM users WHERE login = ?", resultSet -> {
            if (!resultSet.next()) {
                return null;
            }
            return new UserProfile(resultSet.getString("login"), resultSet.getString("password"));
        }, login);
    }
}
