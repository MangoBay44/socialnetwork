package com.getjavajob.training.web1902.koryukinr.dao;

import com.getjavajob.training.web1902.koryukinr.common.Account;
import com.getjavajob.training.web1902.koryukinr.common.Friend;
import com.getjavajob.training.web1902.koryukinr.common.Group;
import com.getjavajob.training.web1902.koryukinr.common.Status;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.dao.util.ConnectionPool;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FriendDAO {
    private static final String TABLE_NAME_FRIENDSHIP = "friendship";
    private static final String ADD_FRIEND1 = "INSERT INTO " + TABLE_NAME_FRIENDSHIP + " (AccountIdFrom, AccountIdTo, Status)" + "VALUES (?, ?, ?)";
    private static final String ADD_FRIEND2 = "INSERT INTO " + TABLE_NAME_FRIENDSHIP + " (AccountIdTo, AccountIdFrom, Status)" + "VALUES (?, ?, ?)";
    private static final String DELETE_FRIEND1 = "DELETE FROM " + TABLE_NAME_FRIENDSHIP + " WHERE AccountIdFrom = ? AND AccountIdTo = ? AND  Status = ?";
    private static final String DELETE_FRIEND2 = "DELETE FROM " + TABLE_NAME_FRIENDSHIP + " WHERE AccountIdTo = ? AND AccountIdFrom = ? AND  Status = ?";
    private static final String SELECT_ALL_FRIEND = "SELECT * FROM account a INNER JOIN " + TABLE_NAME_FRIENDSHIP + " f ON a.id = f.AccountIdTo " +
            "WHERE AccountIdFrom = ?;";

    private Properties properties;

    public FriendDAO() throws DAOException {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
        } catch (IOException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    public FriendDAO(Properties properties) {
        this.properties = properties;
    }

    public void insert(Friend friendship) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_FRIEND1)) {
            preparedStatement.setInt(1, friendship.getAccountFrom().getId());
            preparedStatement.setInt(2, friendship.getAccountTo().getId());
            preparedStatement.setString(3, friendship.getStatus().toString());
            preparedStatement.execute();
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(ADD_FRIEND2)) {
                preparedStatement1.setInt(1, friendship.getAccountFrom().getId());
                preparedStatement1.setInt(2, friendship.getAccountTo().getId());
                preparedStatement1.setString(3, friendship.getStatus().toString());
                preparedStatement1.execute();
            }
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

    public void delete(Friend friendship) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_FRIEND1)) {
            preparedStatement.setInt(1, friendship.getAccountFrom().getId());
            preparedStatement.setInt(2, friendship.getAccountTo().getId());
            preparedStatement.setString(3, Status.FRIEND.toString());
            preparedStatement.execute();
            try (PreparedStatement preparedStatement1 = connection.prepareStatement(DELETE_FRIEND2)) {
                preparedStatement1.setInt(1, friendship.getAccountFrom().getId());
                preparedStatement1.setInt(2, friendship.getAccountTo().getId());
                preparedStatement1.setString(3, Status.FRIEND.toString());
                preparedStatement1.execute();
            }
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

    public List<Account> getAllFriends(Account account) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FRIEND)) {
            preparedStatement.setInt(1, account.getId());
            List<Account> friends = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                AccountDAO accountDAO = new AccountDAO();
                while (resultSet.next()) {
                    friends.add(accountDAO.createAccountFromResult(resultSet));
                }
            }
            return friends;
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            ConnectionPool.getPool(properties).close(connection);
        }
    }

    public List<Account> getAllFriends(Group group) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_FRIEND)) {
            preparedStatement.setInt(1, group.getId());
            int k;
            List<Account> friends = new ArrayList<>();
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                AccountDAO accountDAO = new AccountDAO();
                while (resultSet.next()) {
                    friends.add(accountDAO.createAccountFromResult(resultSet));
                }
            }
            return friends;
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        } finally {
            ConnectionPool.getPool(properties).close(connection);
        }
    }
}
