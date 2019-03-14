package com.getjavajob.training.web1902.koryukinr.dao;

import com.getjavajob.training.web1902.koryukinr.dao.exception.DAOException;

import java.util.List;

public abstract class AbstractDAO<T> {
    abstract T getById(int id) throws DAOException;

    abstract void deleteById(int id) throws DAOException;

    abstract void insert(T t) throws DAOException;

    abstract void update(T t) throws DAOException;

    abstract List<T> getAll() throws DAOException;
}
