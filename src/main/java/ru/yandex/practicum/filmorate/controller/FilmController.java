package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {

    private final FilmService service;

    @Autowired
    FilmController(FilmService filmService) {
        this.service = filmService;
    }

    @GetMapping
    List<Film> findAll() {
        return service.getAll();
    }

    @GetMapping("{id}")
    Film find(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping
    Film create(@RequestBody Film film) {
        return service.create(film);
    }

    @PutMapping
    Film update(@RequestBody Film film) {
        return service.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    Film putLike(@PathVariable int id, @PathVariable int userId) {
        return service.putLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        return service.deleteLike(id, userId);
    }

    @GetMapping("popular")
    List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        return service.getPopular(count);
    }
}
