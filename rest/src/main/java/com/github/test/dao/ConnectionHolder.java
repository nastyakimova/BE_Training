package com.github.test.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolder {
    private Connection connection;
    private DataSource dataSource;

    public ConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    Connection getConnection() {
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
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
