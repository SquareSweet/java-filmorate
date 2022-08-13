package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequestMapping("films")
public class FilmController {

    private final HashMap<Integer,Film> films = new HashMap<>();

    @GetMapping
    List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    Film create(@RequestBody Film film) {
        if (isValid(film)) {
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("Некорректно заполнены поля фильма");
        }
    }

    @PutMapping("/{filmId}")
    Film update(@PathVariable Integer filmId, @RequestBody Film film) {
        if (filmId == film.getId() || !isValid(film)) {
            films.put(filmId, film);
            return film;
        } else {
            throw new ValidationException("Некорректно заполнены поля фильма");
        }
    }

    private boolean isValid(Film film) {
        if (film.getName().isBlank()) {
            return false;
        }
        if (film.getDescription().length() > 200) {
            return false;
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            return false;
        }
        if (film.getDuration().isNegative()) {
            return false;
        }
        return true;
    }
}
