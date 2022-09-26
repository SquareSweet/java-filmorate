package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {

    @Autowired
    private FilmController filmController;

    @Test
    void createValid() {
        Film expectedFilm = new Film(
                1,
                "Test film name",
                "*".repeat(200),
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );

        Film actualFilm = filmController.create(expectedFilm);

        assertEquals(expectedFilm, actualFilm, "Фильмы не совпадают");
    }

    @Test
    void createNameEmpty() {
        Film expectedFilm = new Film(
                1,
                "",
                "Test film description",
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.create(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void createDescriptionTooLong() {
        Film expectedFilm = new Film(
                1,
                "Test film Name",
                "*".repeat(201),
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.create(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void createInvalidReleaseDate() {
        Film expectedFilm = new Film(
                1,
                "Test film Name",
                "Test film description",
                LocalDate.of(1800,12,25),
                180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.create(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void createDurationNegative() {
        Film expectedFilm = new Film(
                1,
                "Test film Name",
                "Test film description",
                LocalDate.of(2007,12,25),
                -180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.create(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateValid() {
        Film film = new Film(
                1,
                "Test film name",
                "Test film description",
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );
        filmController.create(film);

        Film expectedFilm = new Film(
                1,
                "Test film updated name",
                "Test film updated description",
                LocalDate.of(2008,12,25),
                180,
                new Mpa(1, "G")
        );
        Film actualFilm = filmController.create(expectedFilm);

        assertEquals(expectedFilm, actualFilm, "Фильмы не совпадают");
    }

    @Test
    void updateInvalidId() {
        Film film = new Film(
                1,
                "Test film name",
                "Test film description",
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );
        filmController.create(film);

        Film expectedFilm = new Film(
                2,
                "Test film updated name",
                "Test film updated description",
                LocalDate.of(2008,12,25),
                180,
                new Mpa(1, "G")
        );

        assertThrows(
                NotFoundException.class,
                () -> filmController.update(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateNameEmpty() {
        Film film = new Film(
                1,
                "Test film name",
                "Test film description",
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );
        filmController.create(film);

        Film expectedFilm = new Film(
                1,
                "",
                "Test film updated description",
                LocalDate.of(2008,12,25),
                180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.update(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateDescriptionTooLong() {
        Film film = new Film(
                1,
                "Test film name",
                "Test film description",
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );
        filmController.create(film);

        Film expectedFilm = new Film(
                1,
                "Test film updated name",
                "*".repeat(201),
                LocalDate.of(2008,12,25),
                180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.update(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateInvalidReleaseDate() {
        Film film = new Film(
                1,
                "Test film name",
                "Test film description",
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );
        filmController.create(film);

        Film expectedFilm = new Film(
                1,
                "Test film updated name",
                "Test film updated description",
                LocalDate.of(1800,12,25),
                180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.update(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateDurationNegative() {
        Film film = new Film(
                1,
                "Test film name",
                "Test film description",
                LocalDate.of(2007,12,25),
                180,
                new Mpa(1, "G")
        );
        filmController.create(film);

        Film expectedFilm = new Film(
                1,
                "Test film updated name",
                "Test film updated description",
                LocalDate.of(2008,12,25),
                -180,
                new Mpa(1, "G")
        );

        assertThrows(
                ValidationException.class,
                () -> filmController.update(expectedFilm),
                "Должно выбрасываться исключение"
        );
    }
}