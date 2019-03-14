package com.getjavajob.training.web1902.koryukinr.dao.util;

import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private static final int POOL_SIZE = 5;
    private static final int TIMEOUT = 0;
    private static ConnectionPool instance;

    private final String URL;
    private final String USER;
    private final String PASS;
    private final String DRIVER;
    private final String NAME_RESOURCES;
    private LinkedBlockingQueue<Connection> pool;

    public ConnectionPool() throws DAOException {
        try {
            NAME_RESOURCES = "database.properties";
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(NAME_RESOURCES));
            DRIVER = properties.getProperty("database.driver");
            URL = properties.getProperty("database.url");
            USER = properties.getProperty("database.name");
            PASS = properties.getProperty("database.pass");
            Class.forName(DRIVER);
            pool = new LinkedBlockingQueue<>(POOL_SIZE);
            createConnection();

//            NAME_RESOURCES = "h2.properties";
//            Properties properties = new Properties();
//            properties.load(getClass().getClassLoader().getResourceAsStream(NAME_RESOURCES));
//            DRIVER = properties.getProperty("jdbc.driver");
//            URL = properties.getProperty("jdbc.url");
//            USER = properties.getProperty("jdbc.username");
//            PASS = properties.getProperty("jdbc.password");
//            Class.forName(DRIVER);
//            pool = new LinkedBlockingQueue<>(POOL_SIZE);
//            createConnection();

        } catch (IOException | ClassNotFoundException | InterruptedException | SQLException e) {
            throw new DAOException("Failed ConnectionPool constructor from DAO layer", e);
        }
    }

    public static ConnectionPool getPool() throws DAOException {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
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
