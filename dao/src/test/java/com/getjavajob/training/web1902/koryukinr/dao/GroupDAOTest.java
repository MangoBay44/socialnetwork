package com.getjavajob.training.web1902.koryukinr.dao;

import com.getjavajob.training.web1902.koryukinr.common.Group;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.dao.util.ConnectionPool;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GroupDAOTest {
    private GroupDAO groupDAO;
    private Group group;

    @Before
    public void init() throws IOException, DAOException, SQLException {
        File file = new File(GroupDAO.class.getResource("/create.sql").getPath());
        Reader reader = new FileReader(file);

        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("h2.properties"));

        group = new Group(4, "Sport");
        Connection connection = ConnectionPool.getPool(properties).getConnection();
        RunScript.execute(connection, reader);
        connection.commit();
        ConnectionPool.getPool(properties).close(connection);
        groupDAO = new GroupDAO();
    }

    @Test
    public void insert() throws DAOException {
        groupDAO.insert(group);

        assertEquals("Sport", groupDAO.getById(4).getName());
    }

    @Test
    public void update() throws DAOException {
        groupDAO.insert(group);

        group.setName("Dance");
        groupDAO.update(group);
        assertEquals("Dance", groupDAO.getById(4).getName());
    }

    @Test
    public void get() throws DAOException {
        assertEquals("Programming", groupDAO.getById(3).getName());
    }

    @Test
    public void getAll() throws DAOException {
        List<Group> groups = groupDAO.getAll();
        assertEquals(3, groups.size());
    }

    @Test
    public void delete() throws DAOException {
        groupDAO.deleteById(1);

        Group group = groupDAO.getById(1);
        assertNull(group);
    }
}
