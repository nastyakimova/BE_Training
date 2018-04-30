package com.github.test.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolder {
    private Connection connection = null;
    private DataSource dataSource;

    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    Connection getConnection() throws SQLException {
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    void rollback() {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }

    void commit() {
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }

    void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        }
    }
}
