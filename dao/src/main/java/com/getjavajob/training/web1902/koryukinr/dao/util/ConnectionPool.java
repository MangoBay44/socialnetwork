package com.getjavajob.training.web1902.koryukinr.dao.util;

import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static final int POOL_SIZE = 5;
    private static final int TIMEOUT = 0;
    private static ConnectionPool instance;

    private static String URL;
    private static String USER;
    private static String PASS;
    private static String DRIVER;

    private LinkedBlockingQueue<Connection> pool;

    public ConnectionPool(Properties properties) throws DAOException {
        try {
            DRIVER = properties.getProperty("driver");
            URL = properties.getProperty("url");
            USER = properties.getProperty("name");
            PASS = properties.getProperty("pass");
            Class.forName(DRIVER);
            pool = new LinkedBlockingQueue<>(POOL_SIZE);
            createConnection();
        } catch (InterruptedException | SQLException | ClassNotFoundException e) {
            throw new DAOException("Failed ConnectionPool constructor from DAO layer", e);
        }
    }

    public static ConnectionPool getPool(Properties properties) throws DAOException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool(properties);
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws DAOException {
        try {
            Connection connection = pool.take();
            if (!connection.isValid(TIMEOUT)) {
                connection.close();
                connection = newConnection();
            }
            return connection;
        } catch (SQLException | InterruptedException e) {
            throw new DAOException("Failed get connection from DAO layer", e);
        }
    }

    public void close(Connection connection) throws DAOException {
        if (connection != null) {
            try {
                pool.put(connection);
            } catch (InterruptedException e) {
                throw new DAOException("Failed close connection from DAO layer", e);
            }
        }
    }

    private void createConnection() throws SQLException, InterruptedException {
        Connection connection;
        for (int i = 0; i < POOL_SIZE; i++) {
            connection = newConnection();
            pool.put(connection);
        }
    }

    private Connection newConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASS);
        connection.setAutoCommit(false);
        return connection;
    }
}
