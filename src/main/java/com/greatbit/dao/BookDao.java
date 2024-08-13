package com.greatbit.dao;

import com.greatbit.models.Book;

import java.util.List;

public interface BookDao {
    List<Book> findAll();
    Book save(Book book);
    Book getById(long id);
    Book update(Book book);
    void delete(long id);
    void deleteAll();
}
