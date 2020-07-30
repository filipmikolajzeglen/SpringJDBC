package pl.filipzeglen.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import pl.filipzeglen.model.User;
import pl.filipzeglen.util.ConnectionProvider;

public class MysqlUserDao implements UserDao {

    private final static String CREATE =
            "INSERT INTO user(pesel, firstname, lastname) VALUES(:pesel, :firstName, :lastName);";
    private final static String READ =
            "SELECT pesel, firstname, lastname FROM user WHERE pesel = :pesel;";
    private final static String UPDATE =
            "UPDATE user SET pesel=:pesel, firstname=:firstName, lastname=:lastName WHERE pesel = :pesel;";
    private final static String DELETE =
            "DELETE FROM user WHERE pesel=:pesel;";

    private NamedParameterJdbcTemplate template;

    public MysqlUserDao() {
        template = new NamedParameterJdbcTemplate(ConnectionProvider.getDSInstance());
    }

    @Override
    public void create(User user) {
        BeanPropertySqlParameterSource beanParamSource = new BeanPropertySqlParameterSource(user);
        template.update(CREATE, beanParamSource);
    }

    @Override
    public User read(String pesel) {
        User resultUser = null;
        SqlParameterSource namedParameters = new MapSqlParameterSource("pesel", pesel);
        List<User> userList = template.query(READ, namedParameters, BeanPropertyRowMapper.newInstance(User.class));
        if (userList.get(0) != null) {
            resultUser = userList.get(0);
        }
        return resultUser;
    }

    @Override
    public void update(User user) {
        BeanPropertySqlParameterSource beanParamSource = new BeanPropertySqlParameterSource(user);
        template.update(UPDATE, beanParamSource);
    }

    @Override
    public void delete(User user) {
        SqlParameterSource namedParameter = new MapSqlParameterSource("pesel", user.getPesel());
        template.update(DELETE, namedParameter);
    }
}