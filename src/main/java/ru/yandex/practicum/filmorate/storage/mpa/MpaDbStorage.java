package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage{
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa get(int id) {
        try {
            String sql = "SELECT * FROM mpa_ratings WHERE id = ?";

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRow(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинга id=" + id + " не существует");
        }
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "SELECT * FROM mpa_ratings";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRow(rs));
    }

    private Mpa mapRow(ResultSet rs) throws SQLException {
        return new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
