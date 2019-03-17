package com.getjavajob.training.web1902.koryukinr.dao;

import com.getjavajob.training.web1902.koryukinr.common.Account;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.dao.util.ConnectionPool;
import com.getjavajob.training.web1902.koryukinr.dao.util.ProcessingPhoto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class AccountDAO extends AbstractDAO<Account> {
    private static final String TABLE_NAME_ACCOUNT = "account";
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME_ACCOUNT;
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE ID = ?";
    private static final String DELETE_BY_ID = "DELETE FROM " + TABLE_NAME_ACCOUNT + " WHERE ID = ?";
    private static final String INSERT_ALL = "INSERT INTO " + TABLE_NAME_ACCOUNT + " (FirstName, MiddleName, LastName, DateOfBirth, " +
            "WorkPhone, PersonalPhone, HomeAddress, WorkAddress, Email, Skype, AdditionalInformation, Male, Password, Photo, id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ALL = "UPDATE " + TABLE_NAME_ACCOUNT + " SET FirstName = ?, MiddleName = ?," +
            " LastName = ?, DateOfBirth = ?, WorkPhone = ?, PersonalPhone = ?, HomeAddress = ?, WorkAddress = "
            + "?, Email = ?, Skype = ?, AdditionalInformation = ?, Male = ?, Password = ?, Photo = ? WHERE ID = ?";

    private Properties properties;

    public AccountDAO() throws DAOException {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
        } catch (IOException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    public AccountDAO(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Account getById(int id) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (ResultSet resultSet = executeGetOrDelete(id, connection, SELECT_BY_ID).executeQuery()) {
            if (resultSet.next()) {
                return createAccountFromResult(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            ConnectionPool.getPool(properties).close(connection);
        }
    }

    @Override
    public void deleteById(int id) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (PreparedStatement preparedStatement = executeGetOrDelete(id, connection, DELETE_BY_ID)) {
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionPool.getPool(properties).close(connection);
        }
    }

    @Override
    public void insert(Account account) throws DAOException {
        executeInsertOrUpdate(account, INSERT_ALL);
    }

    @Override
    public void update(Account account) throws DAOException {
        executeInsertOrUpdate(account, UPDATE_ALL);
    }

    @Override
    public List<Account> getAll() throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                accounts.add(createAccountFromResult(resultSet));
            }
            return accounts;
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            ConnectionPool.getPool(properties).close(connection);
        }
    }

    Account createAccountFromResult(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        account.setId(resultSet.getInt("id"));
        account.setFirstName(resultSet.getString("FirstName"));
        account.setMiddleName(resultSet.getString("MiddleName"));
        account.setLastName(resultSet.getString("LastName"));
        account.setDateOfBirth(resultSet.getDate("DateOfBirth"));
        account.setPassword(resultSet.getString("Password"));
        account.setMale(resultSet.getString("Male"));
        account.setSkype(resultSet.getString("Skype"));
        account.setEmail(resultSet.getString("Email"));
        account.setWorkPhone(resultSet.getString("WorkPhone"));
        account.setPersonalPhone(resultSet.getString("PersonalPhone"));
        account.setHomeAddress(resultSet.getString("HomeAddress"));
        account.setWorkAddress(resultSet.getString("WorkAddress"));
        account.setAdditionalInfo(resultSet.getString("AdditionalInformation"));
        account.setPhoto(resultSet.getBytes("Photo"));
        return account;
    }

    private void executeInsertOrUpdate(Account account, String query) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, account.getFirstName());
            preparedStatement.setString(2, account.getMiddleName());
            preparedStatement.setString(3, account.getLastName());
            preparedStatement.setDate(4, (Date) account.getDateOfBirth());
            preparedStatement.setString(5, account.getWorkPhone());
            preparedStatement.setString(6, account.getPersonalPhone());
            preparedStatement.setString(7, account.getHomeAddress());
            preparedStatement.setString(8, account.getWorkAddress());
            preparedStatement.setString(9, account.getWorkPhone());
            preparedStatement.setString(10, account.getEmail());
            preparedStatement.setString(11, account.getSkype());
            preparedStatement.setString(12, account.getMale());
            preparedStatement.setString(13, account.getPassword());
            preparedStatement.setBlob(14, new ProcessingPhoto().createPhoto(account.getPhoto()));
            preparedStatement.setInt(15, account.getId());
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionPool.getPool(properties).close(connection);
        }
    }

    private PreparedStatement executeGetOrDelete(int id, Connection connection, String query) throws SQLException, DAOException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }
}
