package com.getjavajob.training.web1902.koryukinr.dao;

import com.getjavajob.training.web1902.koryukinr.common.Group;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.dao.util.ConnectionPool;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GroupDAO extends AbstractDAO<Group> {
    private static final String TABLE_NAME = "grp";
    private static final String SELECT_ALL = "SELECT * FROM " + TABLE_NAME;
    private static final String SELECT_BY_ID = SELECT_ALL + " WHERE ID = ?";
    private static final String DELETE_BY_ID = "DELETE FROM " + TABLE_NAME + " WHERE ID = ?";
    private static final String INSERT_ALL = "INSERT INTO " + TABLE_NAME + " (Name, id) VALUES (?, ?)";
    private static final String UPDATE_ALL = "UPDATE " + TABLE_NAME + " SET Name = ? WHERE ID = ?";

    private Properties properties;

    public GroupDAO() throws DAOException {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("mysql.properties"));
        } catch (IOException e) {
            throw new DAOException("Failed create constructor GroupDAO from DAO layer", e);
        }
    }

    public GroupDAO(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Group getById(int id) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (ResultSet resultSet = executeGetOrDelete(id, connection, SELECT_BY_ID).executeQuery()) {
            if (resultSet.next()) {
                return createGroupFromResultSet(resultSet);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("Failed return GroupDAO from DAO layer", e);
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
            throw new DAOException("Failed delete GroupDAO from DAO layer", e);
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
    public void insert(Group group) throws DAOException {
        executeInsertOrUpdate(group, INSERT_ALL);
    }

    @Override
    public void update(Group group) throws DAOException {
        executeInsertOrUpdate(group, UPDATE_ALL);
    }

    @Override
    public List<Group> getAll() throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (ResultSet resultSet = connection.createStatement().executeQuery(SELECT_ALL)) {
            List<Group> groups = new ArrayList<>();
            while (resultSet.next()) {
                groups.add(createGroupFromResultSet(resultSet));
            }
            return groups;
        } catch (SQLException e) {
            throw new DAOException("Failed return all groups from DAO layer", e);
        } finally {
            ConnectionPool.getPool(properties).close(connection);
        }
    }

    private Group createGroupFromResultSet(ResultSet resultSet) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getInt("id"));
        group.setName(resultSet.getString("Name"));
        return group;
    }

    private PreparedStatement executeGetOrDelete(int id, Connection connection, String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    private void executeInsertOrUpdate(Group group, String query) throws DAOException {
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, group.getName());
            preparedStatement.setInt(2, group.getId());
            preparedStatement.execute();
            connection.commit();
        } catch (SQLException e) {
            throw new DAOException("Failed Insert of Update group from DAO layer", e);
        } finally {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ConnectionPool.getPool(properties).close(connection);
        }
    }
}
