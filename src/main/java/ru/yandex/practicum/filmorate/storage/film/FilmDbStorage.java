package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
@Qualifier("dbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, mpa, release_date, duration) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getMpa().getId());
            stmt.setDate(4, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(5, film.getDuration());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());

        String ratingSql = "SELECT * FROM mpa_ratings WHERE id = ?";
        film.setMpa(jdbcTemplate.queryForObject(
                ratingSql,
                (ratingRs, rowNum) -> mapRowMpa(ratingRs),
                film.getMpa().getId()
        ));

        updateGenres(film.getId(), film.getGenres());
        updateLikes(film.getId(), film.getLikes());

        return film;
    }

    @Override
    public Film update(Film film) {
        try {
            String sql = "SELECT * FROM films WHERE id = ?";
            jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowFilm(rs), film.getId());

            sql = "UPDATE films SET " +
                    "name = ?, " +
                    "description = ?, " +
                    "mpa = ?, " +
                    "release_date = ?, " +
                    "duration = ? " +
                    "WHERE id = ?";

            jdbcTemplate.update(
                    sql,
                    film.getName(),
                    film.getDescription(),
                    film.getMpa().getId(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getId()
            );

            String ratingSql = "SELECT * FROM mpa_ratings WHERE id = ?";
            film.setMpa(jdbcTemplate.queryForObject(
                    ratingSql,
                    (ratingRs, rowNum) -> mapRowMpa(ratingRs),
                    film.getMpa().getId()
            ));

            updateGenres(film.getId(), film.getGenres());
            updateLikes(film.getId(), film.getLikes());

            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильма id=" + film.getId() + " не существует");
        }
    }

    @Override
    public Film get(int id) {
        try {
            String sql = "SELECT * FROM films WHERE id = ?";

            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowFilm(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильма id=" + id + " не существует");
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films";

        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowFilm(rs));
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM films WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    private void updateGenres(int filmId, Set<Genre> genres) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";

        jdbcTemplate.update(sql, filmId);

        sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, genres, genres.size(),
                (ps, genre) -> {
                    ps.setInt(1, filmId);
                    ps.setInt(2, genre.getId());
                });
    }

    private void updateLikes(int filmId, Set<Integer> likes) {
        String sql = "DELETE FROM film_likes WHERE film_id = ?";

        jdbcTemplate.update(sql, filmId);

        sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";

        jdbcTemplate.batchUpdate(sql, likes, likes.size(),
                (ps, userId) -> {
                    ps.setInt(1, filmId);
                    ps.setInt(2, userId);
                });
    }

    private Film mapRowFilm(ResultSet rs) throws SQLException {
        String ratingSql = "SELECT * FROM mpa_ratings WHERE id = ?";

        Film film = new Film(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                jdbcTemplate.queryForObject(
                        ratingSql,
                        (ratingRs, rowNum) -> mapRowMpa(ratingRs),
                        rs.getInt("mpa")
                )
        );

        String sql = "SELECT * FROM film_genre WHERE film_id = ?";
        String genreSql = "SELECT * FROM genres WHERE id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (genreRows.next()) {
            film.getGenres().add(jdbcTemplate.queryForObject(
                    genreSql,
                    (genreRs, rowNum) -> mapRowGenre(genreRs),
                    genreRows.getInt("genre_id")
            ));
        }

        sql = "SELECT * FROM film_likes WHERE film_id = ?";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (likeRows.next()) {
            film.getLikes().add(likeRows.getInt("user_id"));
        }

        return film;
    }

    private Genre mapRowGenre(ResultSet rs) throws SQLException {
        return new Genre(
                rs.getInt("id"),
                rs.getString("name")
        );
    }

    private Mpa mapRowMpa(ResultSet rs) throws SQLException {
        return new Mpa(
                rs.getInt("id"),
                rs.getString("name")
        );
    }
}
