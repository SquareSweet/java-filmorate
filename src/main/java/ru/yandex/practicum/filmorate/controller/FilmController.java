package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RequestMapping("films")
public class FilmController {

    private final HashMap<Integer,Film> films = new HashMap<>();

    @GetMapping
    List<Film> findAll() {
        log.info("Получен запрос на полный список фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    Film create(@RequestBody Film film) {
        if (isValid(film)) {
            films.put(film.getId(), film);
            log.info("Добавлен фильм id={}", film.getId());
            return film;
        } else {
            throw new ValidationException("Некорректно заполнены поля фильма");
        }
    }

    @PutMapping("/{filmId}")
    Film update(@PathVariable Integer filmId, @RequestBody Film film) {
        if (filmId != film.getId()) {
            log.warn("Ошибка при обновлении фильма: не совпадают id");
            throw new ValidationException("Не совпадают id фильма");
        } else if (!isValid(film)) {
            throw new ValidationException("Некорректно заполнены поля пользователя");
        } else {
            films.put(filmId, film);
            log.info("Обновлён фильм id={}", filmId);
            return film;
        }
    }

    private boolean isValid(Film film) {
        if (film.getName().isBlank()) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: пустое название", film.getId());
            return false;
        }
        if (film.getDescription().length() > 200) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: длина описания больше 200 символов", film.getId());
            return false;
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: некорректная дата релиза", film.getId());
            return false;
        }
        if (film.getDuration().isNegative()) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: некорректная продолжительность", film.getId());
            return false;
        }
        return true;
    }
}
