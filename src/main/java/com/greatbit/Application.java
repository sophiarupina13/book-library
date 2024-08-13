package com.greatbit;

import com.greatbit.models.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
public class Application {

    @Bean
    public DataSource dataSource(@Value("${jdbcUrl}") String jdbcUrl) {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl(jdbcUrl);
        dataSource.setUser("user");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Bean
    public CommandLineRunner cmd(DataSource dataSource) {
        return args -> {
            try (InputStream inputStream = this.getClass().getResourceAsStream("/initial.sql")) {
                String sql = new String(inputStream.readAllBytes());
                try (
                    Connection connection = dataSource.getConnection();
                    Statement statement = connection.createStatement()) {
                    statement.executeUpdate(sql);

                    String insertSql = "INSERT INTO books (name, author, pages) VALUES (?, ?, ?)";

                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
                        preparedStatement.setString(1, "java book");
                        preparedStatement.setString(2, "product star");
                        preparedStatement.setInt(3, 100);
                        preparedStatement.executeUpdate();
                    }

                    System.out.println("Printing books from db...");
                    ResultSet resultSet = statement.executeQuery("SELECT id, name, author, pages FROM books");
                    while (resultSet.next()) {
                        Book book = new Book(resultSet.getLong(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
                        System.out.println(book);
                    }
                }
            }
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}