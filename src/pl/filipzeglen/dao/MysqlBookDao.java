package pl.filipzeglen.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import pl.filipzeglen.model.Book;
import pl.filipzeglen.util.ConnectionProvider;

public class MysqlBookDao implements BookDao {

    private final static String CREATE =
            "INSERT INTO book(isbn, title, description) VALUES(:isbn, :title, :description);";
    private final static String READ =
            "SELECT isbn, title, description FROM book WHERE isbn = :isbn;";
    private final static String UPDATE =
            "UPDATE book SET isbn=:isbn, title=:title, description=:description WHERE isbn = :isbn;";
    private final static String DELETE =
            "DELETE FROM book WHERE isbn=:isbn;";

    private NamedParameterJdbcTemplate template;

    public MysqlBookDao() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDSInstance());
    }

    @Override
    public void create(Book book) {
        BeanPropertySqlParameterSource beanParamSource = new BeanPropertySqlParameterSource(book);
        template.update(CREATE, beanParamSource);
    }

    @Override
    public Book read(String isbn) {
        Book resultBook = null;
        SqlParameterSource namedParameters = new MapSqlParameterSource("isbn", isbn);
        List<Book> bookList = template
                .query(READ, namedParameters, BeanPropertyRowMapper.newInstance(Book.class));
        if (bookList.get(0) != null) {
            resultBook = bookList.get(0);
        }
        return resultBook;
    }

    @Override
    public void update(Book book) {
        BeanPropertySqlParameterSource beanParamSource = new BeanPropertySqlParameterSource(book);
        template.update(UPDATE, beanParamSource);
    }

    @Override
    public void delete(Book book) {
        SqlParameterSource namedParameter = new MapSqlParameterSource("isbn", book.getIsbn());
        template.update(DELETE, namedParameter);
    }
}