package com.getjavajob.training.web1902.koryukinr.dao;

import com.getjavajob.training.web1902.koryukinr.common.Account;
import com.getjavajob.training.web1902.koryukinr.common.Friend;
import com.getjavajob.training.web1902.koryukinr.common.Status;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.dao.util.ConnectionPool;
import org.h2.tools.RunScript;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FriendDAOTest {
    private FriendDAO friendDAO;
    private Account account1;
    private Account account2;
    private Account account3;
    private Account account4;
    private Friend friend1;
    private Friend friend2;
    private Friend friend3;

    @Before
    public void init() throws IOException, SQLException, DAOException {
        File file = new File(FriendDAO.class.getResource("/create.sql").getPath());
        Reader reader = new FileReader(file);

        Connection connection = ConnectionPool.getPool().getConnection();
        RunScript.execute(connection, reader);
        connection.commit();
        ConnectionPool.getPool().close(connection);

        AccountDAO accountDAO = new AccountDAO();
        friendDAO = new FriendDAO();
        account1 = new Account(4, "Roman", "", "", "", "");
        account2 = new Account(5, "Sasha", "", "", "", "");
        account3 = new Account(6, "Misha", "", "", "", "");
        account4 = new Account(7, "Sveta", "", "", "", "");

        accountDAO.insert(account1);
        accountDAO.insert(account2);
        accountDAO.insert(account3);
        accountDAO.insert(account4);

        friend1 = new Friend(account1, account2, Status.FRIEND);
        friend2 = new Friend(account1, account3, Status.FRIEND);

        friendDAO.insert(friend1);
        friendDAO.insert(friend2);
    }

    @Test
    public void getAll() throws DAOException {
        List<Account> friends = friendDAO.getAllFriends(account1);
        assertEquals(2, friends.size());
    }

    @Test
    public void insert() throws DAOException {
        Friend friend = new Friend(account1, account4, Status.FRIEND);
        friendDAO.insert(friend);
        List<Account> friends = friendDAO.getAllFriends(account1);

        assertEquals(3, friends.size());
    }

    @Test
    public void delete() throws DAOException {
        Friend friend = new Friend(account1, account4, Status.FRIEND);
        friendDAO.insert(friend);
        friendDAO.delete(friend);
        List<Account> friends = friendDAO.getAllFriends(account1);

        assertEquals(2, friends.size());
    }
}
