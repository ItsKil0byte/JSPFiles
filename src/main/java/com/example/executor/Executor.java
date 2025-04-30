package com.example.executor;

import java.sql.*;

public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    public void execUpdate(String update, Object... params) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(update)) {
            setParams(statement, params);
            statement.executeUpdate();
        }
    }

    public <T> T execQuery(String query, ResultHandler<T> resultHandler, Object... params) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            setParams(statement, params);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultHandler.handle(resultSet);
            }
        }
    }

    private void setParams(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
