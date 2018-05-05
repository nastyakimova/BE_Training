package com.github.test.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcConnectionHolder {
    private ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();
    private DataSource dataSource;

    public JdbcConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        Connection connection = connectionThreadLocal.get();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
                connection.setAutoCommit(false);
                connectionThreadLocal.set(connection);
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return connection;
    }

    public void rollback() {
        Connection connection = getConnection();
        try {
            connection.rollback();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }

    public void commit() {
        Connection connection = getConnection();
        try {
            connection.commit();
        } catch (SQLException ex) {
            throw new RuntimeException();
        }
    }

    public void close() {
        Connection connection = connectionThreadLocal.get();
        if (connection != null) {
            try {
                connection.close();
                connectionThreadLocal.set(null);
            } catch (SQLException ex) {
                throw new RuntimeException();
            }
        }
    }
}
