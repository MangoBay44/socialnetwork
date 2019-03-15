package com.getjavajob.training.web1902.koryukinr.dao;

import com.getjavajob.training.web1902.koryukinr.common.Account;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.dao.util.ConnectionPool;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.CookieHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AccountDAOTest {
    private AccountDAO accountDAO;
    private Account account;

    @Before
    public void init() throws IOException, SQLException, DAOException {
        File file = new File(AccountDAOTest.class.getResource("/create.sql").getPath());
        Reader reader = new FileReader(file);
        account = new Account(4, "Саша", "Смирнов", "123-321", "89123459219", "London");

        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("h2.properties"));

        Connection connection = ConnectionPool.getPool(properties).getConnection();
        RunScript.execute(connection, reader);
        connection.commit();
        ConnectionPool.getPool(properties).close(connection);
        accountDAO = new AccountDAO(properties);
    }

    @Test
    public void insert() throws DAOException {
        accountDAO.insert(account);
        assertEquals("Саша", accountDAO.getById(4).getFirstName());
    }

    @Test
    public void update() throws DAOException {
        accountDAO.insert(account);

        account.setFirstName("Александр");
        accountDAO.update(account);
        assertEquals("Александр", accountDAO.getById(4).getFirstName());
    }

    @Test
    public void get() throws DAOException {
        assertEquals("Маша", accountDAO.getById(3).getFirstName());
    }

    @Test
    public void getAll() throws DAOException {
        List<Account> accounts = accountDAO.getAll();
        assertEquals(3, accounts.size());
    }

    @Test
    public void delete() throws DAOException {
        accountDAO.deleteById(1);
        Account account = accountDAO.getById(1);

        assertNull(account);
    }
}
