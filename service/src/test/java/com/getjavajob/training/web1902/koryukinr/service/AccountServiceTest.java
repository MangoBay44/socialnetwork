package com.getjavajob.training.web1902.koryukinr.service;

import com.getjavajob.training.web1902.koryukinr.common.Account;
import com.getjavajob.training.web1902.koryukinr.common.Friend;
import com.getjavajob.training.web1902.koryukinr.common.Status;
import com.getjavajob.training.web1902.koryukinr.dao.AccountDAO;
import com.getjavajob.training.web1902.koryukinr.dao.FriendDAO;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.service.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class AccountServiceTest {
    private AccountDAO accountDAO;
    private FriendDAO friendDAO;
    private AccountService accountService;

    private Account account1 = new Account(1, "Ivan", "", "", "", "");
    private Account account2 = new Account(2, "Dasha", "", "", "", "");
    private Account account3 = new Account(3, "Masha", "", "", "", "");
    private Account account4 = new Account(4, "Sasha", "", "", "", "");

    private Friend friend1 = new Friend(account1, account2, Status.FRIEND);
    private Friend friend2 = new Friend(account1, account3, Status.FRIEND);

    @Before
    public void init() {
        accountDAO = mock(AccountDAO.class);
        friendDAO = mock(FriendDAO.class);
        accountService = new AccountService(accountDAO, friendDAO);
    }

    @Test
    public void createAccount() throws ServiceException, DAOException {
        accountService.createAccount(account1);
        verify(accountDAO).insert(account1);
        accountService.createAccount(account2);
        verify(accountDAO).insert(account2);
        accountService.createAccount(account3);
        verify(accountDAO).insert(account3);
        accountService.createAccount(account4);
        verify(accountDAO).insert(account4);
        verifyNoMoreInteractions(accountDAO);
    }

    @Test
    public void updateAccount() throws ServiceException, DAOException {
        accountService.updateAccount(account1);
        verify(accountDAO).update(account1);
        verifyNoMoreInteractions(accountDAO);
    }

    @Test
    public void deleteAccount() throws DAOException, ServiceException {
        accountService.deleteAccount(account1);
        verify(accountDAO).deleteById(account1.getId());
        verifyNoMoreInteractions(accountDAO);
    }

    @Test
    public void addFriend() throws DAOException, ServiceException {
        accountService.addFriend(friend1);
        verify(friendDAO).insert(friend1);
        verifyNoMoreInteractions(friendDAO);
    }

    @Test
    public void deleteFriend() throws ServiceException, DAOException {
        accountService.addFriend(friend1);
        verify(friendDAO).insert(friend1);
        verifyNoMoreInteractions(friendDAO);

        accountService.deleteFriend(friend1);
        verify(friendDAO).delete(friend1);
        verifyNoMoreInteractions(friendDAO);
    }

    @Test
    public void getAllFriends() throws ServiceException, DAOException {
        accountService.addFriend(friend1);
        verify(friendDAO).insert(friend1);
        accountService.addFriend(friend2);
        verify(friendDAO).insert(friend2);

        List<Account> friends = new ArrayList<>();
        friends.add(account2);
        friends.add(account3);
        when(friendDAO.getAllFriends(account1)).thenReturn(friends);
        assertEquals(friends.size(), accountService.getFriends(account1).size());
    }
}
