package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("dbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public User create(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);

        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public User update(User user) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), user.getId());

            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }

            sql = "UPDATE users SET " +
                    "email = ?, " +
                    "login = ?, " +
                    "name = ?, " +
                    "birthday = ? " +
                    "WHERE id = ?";

            jdbcTemplate.update(
                    sql,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId()
            );

            updateFriends(user.getId(), user.getFriends());

            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя id=" + user.getId() + " не существует");
        }
    }

    @Override
    public User get(int id) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователя id=" + id + " не существует");
        }
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    private void updateFriends(int userId, Set<Integer> friends) {
        String sql = "DELETE FROM user_friend WHERE user_id = ?";

        jdbcTemplate.update(sql, userId);

        sql = "INSERT INTO user_friend (user_id, friend_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, friends, friends.size(),
                (ps, friendId) -> {
                    ps.setInt(1, userId);
                    ps.setInt(2, friendId);
                });
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate()
        );

        String sql = "SELECT * FROM user_friend WHERE user_id = ?";

        SqlRowSet friendRows = jdbcTemplate.queryForRowSet(sql, user.getId());
        while (friendRows.next()) {
            user.getFriends().add(friendRows.getInt("friend_id"));
        }

        return user;
    }
}
