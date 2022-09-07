package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {

    private final FilmStorage filmStorage;

    @Autowired
    FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    List<Film> findAll() {
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    Film find(@PathVariable int id) {
        return filmStorage.get(id);
    }

    @PostMapping
    Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping
    Film update(@RequestBody Film film) {
        return filmStorage.update(film);
    }
}
