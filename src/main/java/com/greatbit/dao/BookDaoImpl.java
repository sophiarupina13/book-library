package com.greatbit.dao;

import com.greatbit.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Component
public class BookDaoImpl implements BookDao {

    private final DataSource dataSource;

    @Autowired
    public BookDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public List<Book> findAll() {
        final String sql = "select id, name, author, pages from books";
        List<Book> books = new LinkedList<>();

        try (
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Book book = new Book(rs.getInt("id"), rs.getString("name"), rs.getString("author"), rs.getInt("pages"));
                books.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return books;
    }

    @Override
    public Book save(Book book) {
        final String sql = "insert into books (name, author, pages) values (?, ?, ?)";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPages());
            preparedStatement.executeUpdate();
            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    book.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return book;
    }

    @Override
    public Book getById(long id) {
        final String sql = "select id, name, author, pages from books where id = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, id);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (!rs.next()) {
                    throw new RuntimeException("Book with id " + id + " not found");
                }
                return new Book(rs.getLong("id"), rs.getString("name"), rs.getString("author"), rs.getInt("pages"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book update(Book book) {
        if (getById(book.getId()) == null) {
            throw new RuntimeException("Book with id " + book.getId() + " not found");
        }
        final String sql = "update books set name = ?, author = ?, pages = ? where id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setInt(3, book.getPages());
            preparedStatement.setLong(4, book.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return book;
    }

    @Override
    public void delete(long id) {
        if (getById(id) == null) {
            throw new RuntimeException("Book with id " + id + " not found");
        }
        final String sql = "delete from books where id = ?";

        try (
            Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAll() {
        final String sql = "truncate table books";

        try (
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
