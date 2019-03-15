package com.getjavajob.training.web1902.koryukinr.service;

import com.getjavajob.training.web1902.koryukinr.common.Account;
import com.getjavajob.training.web1902.koryukinr.common.Friend;
import com.getjavajob.training.web1902.koryukinr.dao.AccountDAO;
import com.getjavajob.training.web1902.koryukinr.dao.FriendDAO;
import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;
import com.getjavajob.training.web1902.koryukinr.service.exception.ServiceException;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;
    private FriendDAO friendDAO;

    public AccountService() throws ServiceException {
        try {
            accountDAO = new AccountDAO();
            friendDAO = new FriendDAO();
        } catch (DAOException e) {
            throw new ServiceException("Failed create constructor from service layer", e);
        }
    }

    public AccountService(AccountDAO accountDAO, FriendDAO friendDAO) {
        this.accountDAO = accountDAO;
        this.friendDAO = friendDAO;
    }

    public void createAccount(Account account) throws ServiceException {
        try {
            accountDAO.insert(account);
        } catch (DAOException e) {
            throw new ServiceException("Failed create account from service layer", e);
        }
    }

    public void updateAccount(Account account) throws ServiceException {
        try {
            accountDAO.update(account);
        } catch (DAOException e) {
            throw new ServiceException("Failed update account from service layer", e);
        }
    }

    public void deleteAccount(Account account) throws ServiceException {
        try {
            accountDAO.deleteById(account.getId());
        } catch (DAOException e) {
            throw new ServiceException("Failed delete account from service layer", e);
        }
    }

    public List<Account> getAllAccounts() throws ServiceException {
        try {
            return accountDAO.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed get all accounts from service layer", e);
        }
    }

    public void addFriend(Friend friend) throws ServiceException {
        try {
            friendDAO.insert(friend);
        } catch (DAOException e) {
            throw new ServiceException("Failed add friend from service layer", e);
        }
    }

    public void deleteFriend(Friend friend) throws ServiceException {
        try {
            friendDAO.delete(friend);
        } catch (DAOException e) {
            throw new ServiceException("Failed delete friend from service layer", e);
        }
    }

    public List<Account> getFriends(Account account) throws ServiceException {
        try {
            return friendDAO.getAllFriends(account);
        } catch (DAOException e) {
            throw new ServiceException("Failed get all friends from service layer", e);
        }
    }
}
