package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/{filmId}")
    Film update(@PathVariable Integer filmId, @RequestBody Film film) {
        if (filmId == film.getId()) {
            films.put(filmId, film);
            return film;
        } else {
            throw new ValidationException("Не совпадают id фильмов");
        }
    }
}
