package com.greatbit.dao;

import com.greatbit.models.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(
        properties = {"jdbcUrl=jdbc:h2:mem:db;DB_CLOSE_DELAY=-1"}
)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDaoImplTest {

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void setUp() {
        bookDao.deleteAll();
    }

    @Test
    void findAll() {
        Assertions.assertTrue(bookDao.findAll().isEmpty());
        Book book = bookDao.save(new Book("book name", "book author", 10));
        bookDao.save(book);
        Assertions.assertFalse(bookDao.findAll().isEmpty());
    }

    @Test
    void save() {
        Book book = bookDao.save(new Book("book name", "book author", 10));
        List<Book> books = bookDao.findAll();
        Assertions.assertTrue(book.getId() > 0);
        Assertions.assertTrue(books.contains(book));
    }

    @Test
    void getById() {
        Assertions.assertThrows(RuntimeException.class, () -> bookDao.getById(-1));
        Book book = bookDao.save(new Book(1, "book name", "book author", 10));
        bookDao.save(new Book(2, "book name 2", "book author 2", 10));

        Assertions.assertNotNull(bookDao.getById(book.getId()));
        Assertions.assertEquals(book.getName(), bookDao.getById(book.getId()).getName());
    }

    @Test
    void update() {
        Book book = bookDao.save(new Book(1, "book name", "book author", 10));
        book.setName("new name");

        bookDao.update(book);
        Assertions.assertEquals("new name", bookDao.getById(book.getId()).getName());

        Assertions.assertThrows(RuntimeException.class, () -> bookDao.update(new Book("book name", "book author", 10)));
    }

    @Test
    void delete() {
        Book book = bookDao.save(new Book(1, "book name", "book author", 10));
        Book book2 = bookDao.save(new Book(2, "book name 2", "book author 2", 10));

        bookDao.delete(book.getId());

        Assertions.assertFalse(bookDao.findAll().contains(book));
        Assertions.assertTrue(bookDao.findAll().contains(book2));
        Assertions.assertThrows(RuntimeException.class, () -> bookDao.getById(book.getId()));
    }

    @Test
    void deleteAll() {
        Assertions.assertFalse(bookDao.findAll().isEmpty());
        bookDao.deleteAll();
        Assertions.assertTrue(bookDao.findAll().isEmpty());
    }
}