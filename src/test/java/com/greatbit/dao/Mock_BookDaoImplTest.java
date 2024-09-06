package com.greatbit.dao;

import com.greatbit.models.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Mock_BookDaoImplTest {

    @Mock
    private DataSource dataSource;

    @InjectMocks
    private BookDaoImpl bookDaoImpl;

    @Test
    void findAllTest() throws SQLException {

        Connection connection = Mockito.mock(Connection.class);
        Statement statement = Mockito.mock(Statement.class);
        ResultSet resultSet = Mockito.mock(ResultSet.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("java book");
        when(resultSet.getString("author")).thenReturn("product star");
        when(resultSet.getInt("pages")).thenReturn(100);

        List<Book> books = bookDaoImpl.findAll();
        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("java book", books.get(0).getName());

    }


}
